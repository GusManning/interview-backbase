package com.backbase;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class BackbaseQ3IntegrationTest {

	public MockMvc mvc;
	
	@Autowired
	public WebApplicationContext webApplicationContext;
	
	@BeforeEach
	public void setupMcvMock() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	@Order(1)
	public void testPostQuestion() throws Exception {
		StringBuilder json = new StringBuilder();
		json.append("{\r\n");
		json.append("  \"author\": \"Daniel\",\r\n");
		json.append("  \"message\": \"Message text\"\r\n");
		json.append("}");
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/questions")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json.toString())).andReturn();
		Assertions.assertEquals(201, result.getResponse().getStatus());
		Assertions.assertEquals("{\"id\":1,\"author\":\"Daniel\",\"message\":\"Message text\",\"replies\":0}"
				, result.getResponse().getContentAsString());
	}
	
	@Test
	@Order(2)
	public void testPostReply() throws Exception {
		StringBuilder json = new StringBuilder();
		json.append("{\r\n");
		json.append("  \"author\": \"Reply author\",\r\n");
		json.append("  \"message\": \"Message reply text\"\r\n");
		json.append("}");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/questions/1/reply")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json.toString())).andReturn();
		Assertions.assertEquals(201, result.getResponse().getStatus());
		Assertions.assertEquals("{\"questionId\":1,\"id\":2,\"author\":\"Reply author\",\"message\":\"Message reply text\"}"
				, result.getResponse().getContentAsString());
	}
	
	@Test
	@Order(3)
	public void testGetThread() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/questions/1").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("{\"id\":1,\"author\":\"Daniel\",\"message\":\"Message text\",\"replies\":[{\"id\":2,\"author\":\"Reply author\",\"message\":\"Message reply text\"}]}"
				, result.getResponse().getContentAsString());
	}
	
	@Test
	@Order(4)
	public void TestAllQuestions() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/questions").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("[{\"id\":1,\"author\":\"Daniel\",\"message\":\"Message text\",\"replies\":1}]"
				, result.getResponse().getContentAsString());
	}
	
	@Test
	@Order(5)
	public void failGetThread() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/questions/17").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		Assertions.assertEquals(404, result.getResponse().getStatus());
	}
	
	@Test
	@Order(6)
	public void failPostReply() throws Exception {
		StringBuilder json = new StringBuilder();
		json.append("{\r\n");
		json.append("  \"author\": \"Reply author\",\r\n");
		json.append("  \"message\": \"Message reply text\"\r\n");
		json.append("}");
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/questions/17/reply")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json.toString())).andReturn();
		Assertions.assertEquals(404, result.getResponse().getStatus());
		
		json = new StringBuilder();
		json.append("{\r\n");
		json.append("  \"id\": 1\r\n");
		json.append("  \"author\": \"Reply author\",\r\n");
		json.append("  \"message\": \"Message reply text\"\r\n");
		json.append("}");
		
		result = mvc.perform(MockMvcRequestBuilders.post("/questions/1/reply")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json.toString())).andReturn();
		Assertions.assertEquals(400, result.getResponse().getStatus());
	}
	
	@Test
	@Order(7)
	public void failPostQuestion() throws Exception {
		StringBuilder json = new StringBuilder();
		json.append("{\r\n");
		json.append("  \"id\": 1\r\n");
		json.append("  \"author\": \"Daniel\",\r\n");
		json.append("  \"message\": \"Message text\"\r\n");
		json.append("}");
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/questions")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json.toString())).andReturn();
		Assertions.assertEquals(400, result.getResponse().getStatus());
	}
	
}
