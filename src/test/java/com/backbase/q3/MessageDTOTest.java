package com.backbase.q3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageDTOTest {

	private MessageDTO dto;
	
	@BeforeEach
	public void setup() {
		dto = new MessageDTO(1L, "Author", "Message");
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
	public void testToString() {
		Assertions.assertEquals("MessageDTO(id=1, author=Author, message=Message)", dto.toString());
	}
	
	@Test
	public void testEquals() {
		Assertions.assertTrue(dto.equals(dto));
		Assertions.assertFalse(dto.equals(new Object()));
		Assertions.assertTrue(dto.equals(new MessageDTO(1L, "Author", "Message")));
		Assertions.assertFalse(dto.equals(new MessageDTO(2L, "Author", "Message")));
		Assertions.assertFalse(dto.equals(new MessageDTO(1L, "Buthor", "Message")));
		Assertions.assertFalse(dto.equals(new MessageDTO(1L, "Author", "Smessage")));
	}
	
	@Test
	public void testHash() {
		Assertions.assertEquals(-1261441492, dto.hashCode());
	}
}
