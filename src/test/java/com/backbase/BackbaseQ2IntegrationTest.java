package com.backbase;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class BackbaseQ2IntegrationTest {

	public MockMvc mvc;
	
	@Autowired
	public WebApplicationContext webApplicationContext;
	
	@BeforeEach
	public void setupMcvMock() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	@Order(1)
	public void integrationLongToShort() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/short").param("url", "https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json#38").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("8DD2F75", result.getResponse().getContentAsString());
		
		result = mvc.perform(MockMvcRequestBuilders.get("/short").param("url", "I'm not a url").accept(MediaType.TEXT_PLAIN)).andReturn();
		Assertions.assertEquals(400, result.getResponse().getStatus());
	}
	
	@Test
	@Order(2)
	public void integrationShortToLong() throws Exception {
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/long").param("tiny", "8DD2F75").accept(MediaType.TEXT_PLAIN)).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("https://stash.backbase.com/projects/PO/repos/payment-order-integration-spec/browse/src/main/resources/schemas/definitions.json#38", result.getResponse().getContentAsString());
		
		result = mvc.perform(MockMvcRequestBuilders.get("/long").param("tiny", "FFFFFF").accept(MediaType.TEXT_PLAIN)).andReturn();
		Assertions.assertEquals(404, result.getResponse().getStatus());
	}
	
}
