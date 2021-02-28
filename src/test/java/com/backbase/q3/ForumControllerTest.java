package com.backbase.q3;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@WebMvcTest(ForumController.class)
@EntityScan(basePackages = {"com.backbase.q3"})
public class ForumControllerTest {

	@MockBean
	public ForumService service;
	
	public MockMvc mvc;
	public QuestionEntity questionE;
	public MessageEntity messageE;
	public QuestionDTO questionDTO;
	public MessageDTO messageDTO;
	
	@Autowired
	public WebApplicationContext webApplicationContext;
	
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		questionE = new QuestionEntity();
		questionE.setId(1L);
		questionE.setAuthor("dd");
		questionE.setMessage("ss");
		questionE.setMessages(new ArrayList<MessageEntity>());
		messageE = new MessageEntity();
		messageE.setId(1L);
		messageE.setAuthor("dd");
		messageE.setMessage("dd");
		questionDTO = new QuestionDTO(1L, "", "", 2);
		messageDTO = new MessageDTO(3L, "", "");
	}
	
	@Test
	public void testPostQuestion() throws Exception {
		StringBuilder json = new StringBuilder();
		json.append("{\r\n");
		json.append("  \"author\": \"Daniel\",\r\n");
		json.append("  \"message\": \"Message text\"\r\n");
		json.append("}");
		Mockito.when(service.create(questionE)).thenReturn(questionDTO);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/questions")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json.toString())).andReturn();
		Assertions.assertEquals(201, result.getResponse().getStatus());
		Assertions.assertEquals("", result.getResponse().getContentAsString());
	}
	
	@Test
	public void testPostReply() throws Exception {
		StringBuilder json = new StringBuilder();
		json.append("{\r\n");
		json.append("  \"author\": \"Reply author\",\r\n");
		json.append("  \"message\": \"Message reply text\"\r\n");
		json.append("}");
		Mockito.when(service.addReply(Mockito.anyLong(),Mockito.any())).thenReturn(messageE);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/questions/1/reply")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json.toString())).andReturn();
		Assertions.assertEquals(201, result.getResponse().getStatus());
		Assertions.assertEquals("{\"id\":1,\"author\":\"dd\",\"message\":\"dd\"}", result.getResponse().getContentAsString());
	}
	
	@Test
	public void testGetThread() throws Exception {
		Mockito.when(service.findQuestionsById(1L)).thenReturn(questionE);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/questions/1").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("{\"id\":1,\"author\":\"dd\",\"message\":\"ss\",\"replies\":[]}", result.getResponse().getContentAsString());
	}
	
	@Test
	public void TestAllQuestions() throws Exception {
		Mockito.when(service.findAllQuestions()).thenReturn(List.of(questionDTO));
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/questions").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("[{\"id\":1,\"author\":\"\",\"message\":\"\",\"replies\":2}]", result.getResponse().getContentAsString());
	}
}
