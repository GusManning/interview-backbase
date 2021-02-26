package com.backbase.q1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CourseService {

	@Autowired
	private CourseRepository repository;
	
	public List<CourseEntity> getAllCourses() {
		return emptyListCheck(repository.findAll());
	}
	
	public CourseEntity getCourseById(Long id) {
		Optional<CourseEntity> course = repository.findById(id);
		if (course.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No course found matching ID " + id);
		}
		
		return course.get();
	}
	
	public List<CourseEntity> getCoursesByTitle(String title) {
		return emptyListCheck(repository.findByTitleContaining(title.trim()));
	}
	
	public CourseEntity createCourse(CourseEntity course) {
		if (course == null 
				|| course.getCapacity() == null 
				|| course.getEndDate() == null 
				|| course.getStartDate() == null  
				|| course.getTitle() == null 
				|| course.getTitle().isBlank()
				) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could not parse course information");
		}
		
		if (course.getTitle().length() > CourseEntity.TITLE_LENGTH) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Title exceeds maximum length");
		}
		
		if (course.getId() != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User cannot set Course Id");
		}
		
		if (course.getCapacity() < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Capacity cannot be negative");
		}
		
		if (course.getEndDate().isBefore(course.getStartDate())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"End Date cannot be before Start Date");
		}
		
		course.setTitle(course.getTitle().trim());
		return repository.save(course);
	}
	
	private List<CourseEntity> emptyListCheck(List<CourseEntity> courses) {
		if (courses == null || courses.size() == 0) {
			return new ArrayList<CourseEntity>();
		}
		
		return courses;
	}

	public CourseEntity addRegistration(RegistrationEntity registration) {
		return changeRegistration(registration, false);
	}
	
	public CourseEntity removeRegistration(RegistrationEntity cancelation) {
		return changeRegistration(cancelation, true);
	}
	
	//TODO transactional logic
	private CourseEntity changeRegistration(RegistrationEntity registration, boolean remove) {
		CourseEntity course = this.getCourseById(registration.getCourseId());
		RegistrationEntity entity = registrationCheck(registration, course);
		
		int index = course.getParticipants().indexOf(entity);
		if (remove) {
			if (index == -1) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,entity.getName() + " not enrolled in " + course.getTitle());
			}
			course.getParticipants().remove(index);
		} else {
			if (course.getRemaining() <= 0) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Course is full");
			}
			
			if (index != -1) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,entity.getName() + " already enrolled in " + course.getTitle());
			}
			course.getParticipants().add(entity);
		}
		
		return repository.save(course);
	}
	
	
	private RegistrationEntity registrationCheck(RegistrationEntity entity, CourseEntity course) {
		if (entity.getName() == null || entity.getName().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Name cannot be blank");
		}
		
		if (entity.getRegistrationDate().isAfter(course.getStartDate()) || entity.getRegistrationDate().datesUntil(course.getStartDate()).count() < 4) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cannot change registration status 3 days before or after course has began");
		}
		
		entity.setName(entity.getName().trim());
		return entity;
	}
	
	
}
