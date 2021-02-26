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
	public ResponseEntity<CourseEntity> getCourseById(@PathVariable("id") Long id) {
		return new ResponseEntity<CourseEntity>(courseService.getCourseById(id), new HttpHeaders(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseEntity course) {
		return new ResponseEntity<CourseEntity>(courseService.createCourse(course), new HttpHeaders(), HttpStatus.CREATED);
	}
	

	@PostMapping("/{id}/add")
	public ResponseEntity<CourseEntity> addRegistration(@PathVariable("id") Long courseId, @RequestBody RegistrationEntity registration) {
		CourseEntity course = courseService.addRegistration(checkRegistrationEntity(courseId, registration));
		
		return new ResponseEntity<CourseEntity>(course, new HttpHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("/{id}/remove")
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
