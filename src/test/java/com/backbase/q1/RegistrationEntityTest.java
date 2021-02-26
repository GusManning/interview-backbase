package com.backbase.q1;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.backbase.TestUtil;

public class RegistrationEntityTest {

	public RegistrationEntity entity;
	public LocalDate registrationTime;
	
	@BeforeEach
	public void setup() {
		entity = new RegistrationEntity();
		registrationTime = LocalDate.now();
		entity.setRegistrationDate(registrationTime);
		entity.setName("name");
		entity.setId(1L);
		entity.setCourseId(2L);
	}
	
	@Test
	public void testRegistrationDate() {
		Assertions.assertEquals(registrationTime, entity.getRegistrationDate());
		entity.setRegistrationDate(null);
		Assertions.assertNull(entity.getRegistrationDate());
		entity.setCancelDate(registrationTime.toString());
		Assertions.assertEquals(registrationTime, entity.getRegistrationDate());
	}
	
	@Test
	public void testName() {
		Assertions.assertEquals("name", entity.getName());
	}
	
	@Test
	public void testId() {
		Assertions.assertEquals(1L, entity.getId());
	}
	
	@Test
	public void testCourseId() {
		Assertions.assertEquals(2L,entity.getCourseId());
	}
	
	@Test
	public void testDeserialize() {
		String json1 = "";
		String json2 = "";
		try {
			json1 = TestUtil.getTestUtil().mapToJson(entity);
			entity.setCourseId(null);
			json2 = TestUtil.getTestUtil().mapToJson(entity);
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
		
		Assertions.assertTrue(json1.contains("courseId"));
		Assertions.assertFalse(json2.contains("courseId"));
		
	}
	
	@Disabled("Passes but creates a warning from the Json serialization libraries, might be a bug in libraries caused by LocalDate.")
	@Test
	public void testSerialize() {
		StringBuilder json = new StringBuilder();
		json.append("{\n");
		json.append("\"courseId\": 2,\n");
		json.append("\"cancelDate\": \"2021-05-01\",\n");
		json.append("\"name\": \"Daniel\"\n");
		json.append("}\n");
		
		RegistrationEntity testEntity = null;
		try {
			testEntity = TestUtil.getTestUtil().mapFromJson(json.toString(),RegistrationEntity.class);
		} catch (Exception e) {
			Assertions.fail();
		}
		
		Assertions.assertEquals(2L,testEntity.getCourseId());
		Assertions.assertNotNull(testEntity.getRegistrationDate());
		Assertions.assertEquals("Daniel", testEntity.getName());
	}
	
	@Test
	public void testEquals() {
		RegistrationEntity testEntity = new RegistrationEntity();
		testEntity.setRegistrationDate(null);
		testEntity.setId(5L);
		testEntity.setCourseId(4L);
		Assertions.assertTrue(testEntity.equals(testEntity));
	}
}
