package com.backbase.q3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionDTOTest {

	private QuestionDTO dto;
	
	@BeforeEach
	public void setup() {
		dto = new QuestionDTO(1L, "Author", "Message",0);
	}
	
	@Test
	public void testId() {
		Assertions.assertEquals(1L, dto.getId());
	}
	
	@Test
	public void testAuthor() {
		Assertions.assertEquals("Author", dto.getAuthor());
	}
	
	@Test
	public void testMessage() {
		Assertions.assertEquals("Message", dto.getMessage());
	}
	
	@Test
	public void testReplies() {
		Assertions.assertEquals(0, dto.getReplies());
	}
	
	@Test
	public void testToString() {
		Assertions.assertEquals("QuestionDTO(id=1, author=Author, message=Message, replies=0)", dto.toString());
	}
	
	@Test
	public void testEquals() {
		Assertions.assertTrue(dto.equals(dto));
		Assertions.assertFalse(dto.equals(new Object()));
		Assertions.assertTrue(dto.equals(new QuestionDTO(1L, "Author", "Message",0)));
		Assertions.assertFalse(dto.equals(new QuestionDTO(2L, "Author", "Message",0)));
		Assertions.assertFalse(dto.equals(new QuestionDTO(1L, "Buthor", "Message",0)));
		Assertions.assertFalse(dto.equals(new QuestionDTO(1L, "Author", "Smessage",0)));
		Assertions.assertFalse(dto.equals(new QuestionDTO(1L, "Author", "Message",1)));
	}
	
	@Test
	public void testHash() {
		Assertions.assertEquals(-1249327612, dto.hashCode());
	}
	
}
