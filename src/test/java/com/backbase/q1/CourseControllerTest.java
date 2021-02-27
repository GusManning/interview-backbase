package com.backbase.q1;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.backbase.TestUtil;


@WebMvcTest(CourseController.class)
@ActiveProfiles("test")
@EntityScan(basePackages = {"com.backbase.q1"})
public class CourseControllerTest {
	
	@MockBean
	public CourseService courseService;
	public MockMvc mvc;
	public List<CourseEntity> courses;
	public RegistrationEntity registration;
		
	@Autowired
	public WebApplicationContext webApplicationContext;
	
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		courses = new ArrayList<>();
		CourseEntity course = new CourseEntity();
		course.setCapacity(10);
		course.setEndDate(LocalDate.now().plusDays(50));
		course.setStartDate(LocalDate.now().plusDays(5));
		course.setId(1L);
		course.setParticipants(new ArrayList<>());
		course.setTitle("Test");
		courses.add(course);
		course = new CourseEntity();
		course.setCapacity(15);
		course.setEndDate(LocalDate.now().plusDays(50));
		course.setStartDate(LocalDate.now().plusDays(2));
		course.setId(2L);
		course.setParticipants(new ArrayList<>());
		course.setTitle("Fish");
		courses.add(course);
		registration = new RegistrationEntity();
		registration.setName("Bob");
		registration.setRegistrationDate(LocalDate.now());
	}
	
	
	@Test
	public void testGetAll() throws Exception {
		Mockito.when(courseService.getAllCourses()).thenReturn(courses);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(TestUtil.COURSES_URL).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		
		Assertions.assertEquals(200, result.getResponse().getStatus());
		
		String resultJson = result.getResponse().getContentAsString();
		Assertions.assertTrue(resultJson.contains("Test"));
		Assertions.assertTrue(resultJson.contains("Fish"));
	}
	
	@Test
	public void testGetByTitle() throws Exception {
		Mockito.when(courseService.getCoursesByTitle("Test")).thenReturn(courses);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(TestUtil.COURSES_URL).param("q", "Test").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		Assertions.assertEquals(200, result.getResponse().getStatus());
		
		String resultJson = result.getResponse().getContentAsString();
		Assertions.assertTrue(resultJson.contains("Test"));
	}
	
	@Test
	public void testGetById() throws Exception {
		Mockito.when(courseService.getCourseById(1L)).thenReturn(courses.get(0));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(TestUtil.COURSES_URL+"/1").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		
		Assertions.assertEquals(200, result.getResponse().getStatus());
		
		String resultJson = result.getResponse().getContentAsString();
		Assertions.assertTrue(resultJson.contains("Test"));
	}
	
	@Test
	public void testCreateCourse() throws Exception {
		String json = TestUtil.getTestUtil().getCourseJson("Test Course", "2021-05-01", "2021-05-01", 10);
		
		Mockito.when(courseService.createCourse(Mockito.any())).thenReturn(courses.get(0));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.COURSES_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		
		String resultJson = result.getResponse().getContentAsString();
		Assertions.assertEquals(201, result.getResponse().getStatus());
		Assertions.assertTrue(resultJson.contains("remaining"));
		Assertions.assertTrue(resultJson.contains("participants"));
	}
	
	@Test
	public void testAddRegistration() throws Exception {
		String json = TestUtil.getTestUtil().getRegistrationJson(1L, "2021-05-01", "Daniel");
		
		Mockito.when(courseService.addRegistration(Mockito.any())).thenReturn(courses.get(0));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.ADD_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		
		Assertions.assertEquals(200, result.getResponse().getStatus());
		
		String resultJson = result.getResponse().getContentAsString();
		Assertions.assertTrue(resultJson.contains("Test"));
	}
	
	@Test
	public void testRemoveRegistration() throws Exception {
		String json = TestUtil.getTestUtil().getCancelationJson(1L, "2021-05-01", "Daniel");
		
		Mockito.when(courseService.removeRegistration(Mockito.any())).thenReturn(courses.get(0));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.REMOVE_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		
		Assertions.assertEquals(200, result.getResponse().getStatus());
		String resultJson = result.getResponse().getContentAsString();
		Assertions.assertTrue(resultJson.contains("Test"));
	}
	
	@Test
	public void failGetByTitle() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(TestUtil.COURSES_URL).param("q", " ").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		
		Mockito.verify(courseService,Mockito.times(1)).getAllCourses();
		Mockito.verify(courseService,Mockito.times(0)).getCoursesByTitle(Mockito.anyString());
	}
	
	
	@Test
	public void failAddRegistration() throws Exception {
		registration.setId(2L);
		String json = TestUtil.getTestUtil().mapToJson(registration);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.ADD_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		Assertions.assertEquals(400, result.getResponse().getStatus());
	}
	
	@Test
	public void failRemoveRegistration() throws Exception {
		String json = TestUtil.getTestUtil().getCancelationJson(2L, "2021-05-01", "Daniel");

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.REMOVE_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		Assertions.assertEquals(400, result.getResponse().getStatus());
	}
}
