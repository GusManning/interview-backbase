package com.backbase;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;

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
	
//	public static String getRegistrationJson() {
//		StringBuilder json = new StringBuilder();
//		json.append("{\n");
//		json.append("\"courseId\": 1,\n");
//		json.append("\"registrationDate\": \"2021-05-01\",\n");
//		json.append("\"name\": \"Daniel\"\n");
//		json.append("}\n");
//		return json.toString();
//	}
//	
//	public static String getCancellationJson() {
//		StringBuilder json = new StringBuilder();
//		json.append("{\n");
//		json.append("\"courseId\": 1,\n");
//		json.append("\"cancelDate\": \"2021-05-01\",\n");
//		json.append("\"name\": \"").append("Daniel").append("\"\n");
//		json.append("}\n");
//		return json.toString();
//	}
//	
//	public static String getCourseJson() {
//		StringBuilder json = new StringBuilder();
//		json.append("{\n");
//		json.append("\"title\": \"Course Title\",\n");
//		json.append("\"startDate\": \"2021-05-01\",\n");
//		json.append("\"endDate\": \"2021-05-01\",\n");
//		json.append("\"capacity\": 10\n");
//		json.append("}\n");
//		return json.toString();
//	}
	
	
	
	public String getCourseJson(String title, String startDate, String endDate, Integer capacity) throws JsonProcessingException {
		return mapToJson(new StubCourse(title, startDate, endDate, capacity));
	}
	
	public String getRegistrationJson(Long courseId, String registrationDate, String name) throws JsonProcessingException {
		return mapToJson(new StubRegistry(courseId, null, registrationDate, name));
	}
	
	public String getCancelationJson(Long courseId, String cancelDate, String name) throws JsonProcessingException {
		return mapToJson(new StubRegistry(courseId, cancelDate, null, name));
	}
	
	@Data
	@AllArgsConstructor
	public class StubRegistry {
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private Long courseId;
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private String cancelDate;
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private String registrationDate;
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private String name;
	}
	
	@Data
	@AllArgsConstructor
	public class StubCourse {
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private String title;
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private String startDate;
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private String endDate;
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private Integer capacity;
	}
}
