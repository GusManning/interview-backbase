package com.backbase;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TestUtil {
	
	private static final TestUtil TEST_UTILITY = new TestUtil();
	
	public static final String COURSES_URL = "/courses";
	public static final String ADD_URL = COURSES_URL + "/1/add";
	public static final String REMOVE_URL = COURSES_URL + "/1/remove";
	
	private ObjectMapper mapper;
	
	private TestUtil() {
		mapper = new ObjectMapper();
	}
	
	public static TestUtil getTestUtil() {
		return TEST_UTILITY;
	}
	
	
	public String mapToJson(Object entity) throws JsonProcessingException {
	    return mapper.writeValueAsString(entity);
	}
	
	public <T> T mapFromJson(String json, Class<T> className) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json,className);
	}
	
	public String getCourseJson(String title, String startDate, String endDate, Integer capacity) throws JsonProcessingException {
		return mapToJson(new StubCourse(title, startDate, endDate, capacity));
	}
	
	public String getRegistrationJson(Long courseId, String registrationDate, String name) throws JsonProcessingException {
		return mapToJson(new StubRegistry(courseId, null, registrationDate, name));
	}
	
	public String getCancelationJson(Long courseId, String cancelDate, String name) throws JsonProcessingException {
		return mapToJson(new StubRegistry(courseId, cancelDate, null, name));
	}
	
}
