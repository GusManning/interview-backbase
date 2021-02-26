package com.backbase.q1;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.backbase.TestUtil;

public class CourseEntityTest {

	public CourseEntity courseEntity;
	public LocalDate startDate;
	public LocalDate endDate;
	
	@BeforeEach
	public void setup() {
		courseEntity = new CourseEntity();
		courseEntity.setCapacity(10);
		endDate = LocalDate.now().plusDays(15L);
		courseEntity.setEndDate(endDate);
		courseEntity.setId(1L);
		startDate = LocalDate.now().plusDays(5L);
		courseEntity.setStartDate(startDate);
		courseEntity.setTitle("test");
		courseEntity.setParticipants(new ArrayList<RegistrationEntity>());
	}
	
	@Test
	public void testCapacity() {
		Assertions.assertEquals(10,courseEntity.getCapacity());
		courseEntity.setCapacity(null);
	}
	
	@Test
	public void testRemaining() {
		Assertions.assertEquals(10,courseEntity.getRemaining());
		courseEntity.getParticipants().add(new RegistrationEntity());
		Assertions.assertEquals(9,courseEntity.getRemaining());
		courseEntity.setParticipants(null);
		courseEntity.setSpaceUsed(4);
		Assertions.assertEquals(6,courseEntity.getRemaining());
	}
	
	@Test
	public void testEndDate() {
		Assertions.assertEquals(endDate,courseEntity.getEndDate());
	}
	
	@Test
	public void testStartDate() {
		Assertions.assertEquals(startDate,courseEntity.getStartDate());
	}
	
	@Test
	public void testTitle() {
		Assertions.assertEquals("test", courseEntity.getTitle());
	}
	
	@Test
	public void testParticipants() {
		Assertions.assertEquals(0, courseEntity.getParticipants().size());
		courseEntity.getParticipants().add(new RegistrationEntity());
		Assertions.assertEquals(1, courseEntity.getParticipants().size());
	}
	
	@Test
	public void testSerialization() {
		String json = "";
		try {
			json = TestUtil.getTestUtil().mapToJson(courseEntity);
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
		Assertions.assertTrue(json.contains("participants"));
		courseEntity.setParticipants(null);
		try {
			json = TestUtil.getTestUtil().mapToJson(courseEntity);
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
		}
		Assertions.assertFalse(json.contains("participants"));
	}
	
	
}
