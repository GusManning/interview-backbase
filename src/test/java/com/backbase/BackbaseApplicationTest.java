package com.backbase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class BackbaseApplicationTest {

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
	}
	
	
}
