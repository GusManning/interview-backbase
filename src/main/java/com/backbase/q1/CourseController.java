package com.backbase.q1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	private CourseService courseService;
	
	@GetMapping
	/**
	 * ResponseEntity serves double duty as the access point for getting all courses and courses by title.  Calling /courses?q=""
	 * is effectively the same as calling /courses.  This will return a list of CourseEntity minus their participants possibly filtered by
	 * title if title is specified.  
	 * @param title is a string partial title so "Test" will match "Test Course" and "Test Writing 101", it is case-sensitive.
	 * @return a serialized list of type CourseEntity or empty list.
	 */
	public ResponseEntity<List<CourseEntity>> getCourses(@RequestParam(name="q", required = false, defaultValue = "") String title) {
		List<CourseEntity> courses;
		if (title == null || title.isBlank()) {
			courses = courseService.getAllCourses();
		} else {
			courses = courseService.getCoursesByTitle(title);
		}
		courses.stream().forEach((course)->{
			course.setSpaceUsed(course.getParticipants().size());
			course.setParticipants(null);
			});
		return new ResponseEntity<List<CourseEntity>>(courses, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	/**
	 * @param id 
	 * @return Returns full details on a CourseEntity, unlike getCourses it does not remove participants.  
	 */
	public ResponseEntity<CourseEntity> getCourseById(@PathVariable("id") Long id) {
		return new ResponseEntity<CourseEntity>(courseService.getCourseById(id), new HttpHeaders(), HttpStatus.OK);
	}
	
	@PostMapping
	/**
	 * Creates a new CourseEntity. Requires title, startDate, endDate, and capacity in the message body in order to properly
	 * create a course record.  It is possible to create duplicates, it is not possibly to either specify an ID ahead of time
	 * (that is done by the database) or create a Course that ends before it starts.  See Readme for Json format. 
	 * @param course deserialized CourseEntity
	 * @return CourseEntity with id initialized
	 */
	public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseEntity course) {
		return new ResponseEntity<CourseEntity>(courseService.createCourse(course), new HttpHeaders(), HttpStatus.CREATED);
	}
	

	@PostMapping("/{id}/add")
	/**
	 * Registers a student for an existing course.  If the name, registrationDate, or courseId are null this will fail.
	 * RegistrationDate must not be within 3 days of the course start date or after the startDate.  It is not possible for the 
	 * user to set the registration's id field ahead of time as that is done by the database.  The Id in the Url must match
	 * the courseId in the registration object or this will return 400 and the add will fail.  
	 * @param courseId courseId to register for.
	 * @param registration deserialized RegistrationEntity.
	 * @return Returns an updated CourseEnity object
	 */
	public ResponseEntity<CourseEntity> addRegistration(@PathVariable("id") Long courseId, @RequestBody RegistrationEntity registration) {
		CourseEntity course = courseService.addRegistration(checkRegistrationEntity(courseId, registration));
		
		return new ResponseEntity<CourseEntity>(course, new HttpHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("/{id}/remove")
	/**
	 * Very similar in function to addRegistration.  This removes exiting registrations from CourseEntity objects if that student 
	 * is registered, this will return 404 if they are not found. If the name, registrationDate, or courseId are null this will fail.  The Id in the Url must match
	 * the courseId in the registration object or this will return 400 and the remove will fail.  
	 * @param courseId courseId to register for.
	 * @param cancelation deserialized RegistrationEntity.
	 * @return Returns an updated CourseEnity object
	 */
	public ResponseEntity<CourseEntity> removeRegistration(@PathVariable("id") Long courseId, @RequestBody RegistrationEntity cancelation) {
		CourseEntity course = courseService.removeRegistration(checkRegistrationEntity(courseId, cancelation));
		
		return new ResponseEntity<CourseEntity>(course, new HttpHeaders(), HttpStatus.OK);
	}
	
	private RegistrationEntity checkRegistrationEntity(Long courseId, RegistrationEntity entity) {
		if (entity == null || entity.getCourseId() != courseId) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Body courseId does not match URL");
		}
		
		return entity;
	}
 }
