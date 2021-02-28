package com.backbase.q1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;



@SpringBootTest(classes = { CourseRepository.class, CourseService.class, CourseEntity.class, RegistrationEntity.class })
public class CourseServiceTest {
	
	@MockBean
	public CourseRepository repository;
	
	@Autowired
	public CourseService service;
	
	public CourseEntity course;
	public RegistrationEntity regEntity, cancelEntity;
	public List<CourseEntity> courses;
	public LocalDate now, soon, later, never;
	
	@BeforeEach
	public void setup() {
		now = LocalDate.now();
		soon = LocalDate.now().plusDays(2);
		later = LocalDate.now().plusDays(30);
		never = LocalDate.now().plusYears(1);
		
		course = new CourseEntity();
		course.setCapacity(10);
		course.setEndDate(never);
		course.setStartDate(later);
		course.setTitle("Test");
		course.setParticipants(new ArrayList<RegistrationEntity>());
		courses = new ArrayList<>();
		courses.add(course);
		
		regEntity = new RegistrationEntity();
		regEntity.setName("Test Tom");
		regEntity.setCourseId(1L);
		regEntity.setRegistrationDate(now);
		
		cancelEntity = new RegistrationEntity();
		cancelEntity.setName("Test Tom");
		cancelEntity.setCourseId(1L);
		cancelEntity.setRegistrationDate(now);
	}
	
	
	@Test
	public void testAllCourses() {
		Mockito.when(repository.findAll()).thenReturn(null);
		List<CourseEntity> courses = service.getAllCourses();
		Assertions.assertTrue(courses instanceof ArrayList );
		Assertions.assertEquals(0,courses.size());
	}
	
	@Test 
	public void testCourseById() {
		Mockito.when(repository.findById(2L)).thenReturn(Optional.of(course));
		Assertions.assertEquals(course, service.getCourseById(2L));
	}
	
	@Test
	public void testCourseByTitle() {
		Mockito.when(repository.findByTitleContaining("Test")).thenReturn(courses);
		Assertions.assertEquals(courses, service.getCoursesByTitle("Test"));
	}
	
	@Test
	public void testCreateCourse() {
		course.setTitle("Trim me              ");
		course.setParticipants(null);
		Mockito.when(repository.save(Mockito.any())).thenReturn(course);
		Assertions.assertEquals(course.getTitle().trim(), service.createCourse(course).getTitle());
	}
	
	@Test 
	public void failCourseById() {
		HttpStatus status = null;
		Mockito.when(repository.findById(2L)).thenReturn(Optional.empty());
		try { 
			service.getCourseById(1L);
		} catch (ResponseStatusException rse) {
			status = rse.getStatus();
		}
		Assertions.assertEquals(status, HttpStatus.NOT_FOUND);
	}
	

	
	@Test
	public void testAddRegistration() {
		Mockito.when(repository.findById(1L)).thenReturn(Optional.of(course));
		Mockito.when(repository.save(Mockito.any())).thenReturn(null);
		
		Assertions.assertNull(service.addRegistration(regEntity));
		Assertions.assertEquals(9, course.getRemaining());
	}
	
	@Test
	public void testRemoveRegistration() {
		course.getParticipants().add(regEntity);
		Mockito.when(repository.findById(1L)).thenReturn(Optional.of(course));
		Mockito.when(repository.save(Mockito.any())).thenReturn(null);
		
		Assertions.assertNull(service.removeRegistration(regEntity));
		Assertions.assertEquals(10, course.getRemaining());	
	}
	
	@Test
	public void failAddRegistration() {
		course.getParticipants().add(regEntity);
		Mockito.when(repository.findById(1L)).thenReturn(Optional.of(course));
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, getStatusAdd(regEntity));
		for(int i = 0; i < 9; i++) {
			course.getParticipants().add(new RegistrationEntity());
		}
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, getStatusAdd(regEntity));
	}
	
	private HttpStatus getStatusAdd(RegistrationEntity entity) {
		try { 
			service.addRegistration(entity);
		} catch (ResponseStatusException rse) {
			return rse.getStatus();
		}
		return null;
	}
	
	@Test 
	public void failRemoveRegistration() {
		Mockito.when(repository.findById(1L)).thenReturn(Optional.of(course));
		Assertions.assertEquals(HttpStatus.NOT_FOUND, getStatusRemove(cancelEntity));
	}
	
	private HttpStatus getStatusRemove(RegistrationEntity entity) {
		try { 
			service.removeRegistration(entity);
		} catch (ResponseStatusException rse) {
			return rse.getStatus();
		}
		return null;
	}
	
	@Test
	public void failRegistrationCheck() {
		course.setStartDate(soon);
		Mockito.when(repository.findById(1L)).thenReturn(Optional.of(course));
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, getStatusAdd(regEntity));
		regEntity.setName("   ");
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, getStatusAdd(regEntity));
	}
	
	@Test
	public void failCreate() {
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, getStatusCreate(new CourseEntity()));
		course.setEndDate(now);
		course.setStartDate(later);
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, getStatusCreate(course));
		course.setId(5L);
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, getStatusCreate(course));
		
	}
	
	private HttpStatus getStatusCreate(CourseEntity entity) {
		try { 
			service.createCourse(entity);
		} catch (ResponseStatusException rse) {
			return rse.getStatus();
		}
		return null;
	}
}
