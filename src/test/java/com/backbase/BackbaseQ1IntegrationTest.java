package com.backbase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class BackbaseQ1IntegrationTest {

	public MockMvc mvc;
	
	@Autowired
	public WebApplicationContext webApplicationContext;
	
	@BeforeEach
	public void setupMcvMock() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	
	@Test
	@Order(1)
	public void contextLoads() {
		Assertions.assertTrue(true);
	}
	
	@Test
	@Order(2)
	public void integrationCreateAndFill() throws Exception {
		String json = TestUtil.getTestUtil().getCourseJson("Test Course", "2021-05-01", "2021-05-01", 10);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.COURSES_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		String resultJson = result.getResponse().getContentAsString();
		
		Assertions.assertEquals(201,result.getResponse().getStatus());
		Assertions.assertEquals("{\"id\":1,\"title\":\"Test Course\",\"startDate\":\"2021-05-01\",\"endDate\":\"2021-05-01\",\"capacity\":10,\"remaining\":10}",
				resultJson);
	}
	
	@Test
	@Order(5)
	public void integrationAddRegistration() throws Exception {
		String json = TestUtil.getTestUtil().getRegistrationJson(1L, "2021-04-01", "Daniel");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.ADD_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		String resultJson = result.getResponse().getContentAsString();
		
		Assertions.assertEquals(200,result.getResponse().getStatus());
		Assertions.assertEquals("{\"id\":1,\"title\":\"Test Course\",\"startDate\":\"2021-05-01\",\"endDate\":\"2021-05-01\",\"capacity\":10,\"remaining\":9,\"participants\":[{\"name\":\"Daniel\",\"registrationDate\":\"2021-04-01\"}]}",
				resultJson);
		
	}
	
	@Test
	@Order(4)
	public void integrationGetById() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(TestUtil.COURSES_URL+"/1").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String resultJson = result.getResponse().getContentAsString();
		
		Assertions.assertEquals(200,result.getResponse().getStatus());
		Assertions.assertEquals("{\"id\":1,\"title\":\"Test Course\",\"startDate\":\"2021-05-01\",\"endDate\":\"2021-05-01\",\"capacity\":10,\"remaining\":10,\"participants\":[]}",
				resultJson);
		
		result = mvc.perform(MockMvcRequestBuilders.get(TestUtil.COURSES_URL+"/2").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		Assertions.assertEquals(404,result.getResponse().getStatus());
	}
	
	@Test
	@Order(5)
	public void integrationGetByTitle() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(TestUtil.COURSES_URL).param("q", "Test").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		String resultJson = result.getResponse().getContentAsString();
		
		Assertions.assertEquals(200,result.getResponse().getStatus());
		Assertions.assertEquals("[{\"id\":1,\"title\":\"Test Course\",\"startDate\":\"2021-05-01\",\"endDate\":\"2021-05-01\",\"capacity\":10,\"remaining\":10}]",
				resultJson);
	}
	
	@Test
	@Order(6)
	public void integrationRemoveRegistration() throws Exception {
		String json = TestUtil.getTestUtil().getCancelationJson(1L, "2021-04-01", "Daniel");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.REMOVE_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		String resultJson = result.getResponse().getContentAsString();
		Assertions.assertEquals(200,result.getResponse().getStatus());
		Assertions.assertEquals("{\"id\":1,\"title\":\"Test Course\",\"startDate\":\"2021-05-01\",\"endDate\":\"2021-05-01\",\"capacity\":10,\"remaining\":10,\"participants\":[]}",
				resultJson);
	}
	
	@Test
	@Order(7)
	public void integrationRemoveRegistrationFail() throws Exception {
		// Daniel should not longer be in memory after previous test
		String json = TestUtil.getTestUtil().getCancelationJson(1L, "2021-04-01", "Daniel");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.REMOVE_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();

		Assertions.assertEquals(404,result.getResponse().getStatus());
		
		// bad name
		json = TestUtil.getTestUtil().getCancelationJson(1L, "2021-04-01", " ");
		
		result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.REMOVE_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		
		Assertions.assertEquals(400,result.getResponse().getStatus());
		
		// date to close to start date
		json = TestUtil.getTestUtil().getCancelationJson(1L, "2021-05-01", " ");
		
		result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.REMOVE_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
	}
	
	@Test
	@Order(8)
	public void integrationAddRegistrationFail() throws Exception {
		// adding a very small class
		String json = TestUtil.getTestUtil().getCourseJson("Small Class", "2021-05-01", "2021-05-01", 1);
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.COURSES_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		
		Assertions.assertEquals(201, result.getResponse().getStatus());
		StubCourseFull stub = TestUtil.getTestUtil().mapFromJson(result.getResponse().getContentAsString(), StubCourseFull.class);
		Long id = stub.getId();
		
		// fill class
		json = TestUtil.getTestUtil().getRegistrationJson(id, "2021-04-01", "Daniel");
				
		result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.COURSES_URL + "/"+id.toString()+"/add")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		// add second student for error
		json = TestUtil.getTestUtil().getRegistrationJson(id, "2021-04-01", "Tom");
		result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.COURSES_URL + "/"+id.toString()+"/add")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		Assertions.assertEquals(400, result.getResponse().getStatus());
		
		// add student to course with differing IDs in url and json
		result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.ADD_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		Assertions.assertEquals(400, result.getResponse().getStatus());
		
		// add same student twice to larger course
		json = TestUtil.getTestUtil().getRegistrationJson(1L, "2021-04-01", "Daniel");
		result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.ADD_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		
		result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.ADD_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		Assertions.assertEquals(400, result.getResponse().getStatus());
	}
	
	@Test
	@Order(9)
	public void integrationCreateCourseFail() throws Exception {
		MvcResult result;
		// ends before starts
		String json = TestUtil.getTestUtil().getCourseJson("Test Course", "2021-05-02", "2021-05-01", 10);
		
		result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.COURSES_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		
		Assertions.assertEquals(400,result.getResponse().getStatus());
		
		// no title
		json = TestUtil.getTestUtil().getCourseJson("", "2021-05-01", "2021-05-01", 10);
		
		result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.COURSES_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		
		Assertions.assertEquals(400,result.getResponse().getStatus());
		
		// negative space available
		json = TestUtil.getTestUtil().getCourseJson("Title", "2021-05-01", "2021-05-01", -1);
		result = mvc.perform(MockMvcRequestBuilders.post(TestUtil.COURSES_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		
		Assertions.assertEquals(400,result.getResponse().getStatus());
	}
	
	
}
