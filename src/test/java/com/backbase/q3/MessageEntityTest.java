package com.backbase.q3;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.backbase.TestUtil;

public class MessageEntityTest {
	private MessageEntity entity1;
	private MessageEntity entity2;
	private MessageEntity entity3;
	private MessageEntity entity4;
	private MessageEntity entity5;
	
	@BeforeEach
	public void setup() throws IOException {
		TestUtil util = TestUtil.getTestUtil();
		entity1 = new MessageEntity();
		entity1.setId(1L);
		entity1.setAuthor("Author");
		entity1.setMessage("Message");
		// really lazy clone
		entity2 = util.mapFromJson(util.mapToJson(entity1), MessageEntity.class);
		entity2.setId(1L);
		entity3 = util.mapFromJson(util.mapToJson(entity1), MessageEntity.class);
		entity3.setAuthor("Bother");
		entity4 = util.mapFromJson(util.mapToJson(entity1), MessageEntity.class);
		entity4.setMessage("Coffee");
		entity5 = util.mapFromJson(util.mapToJson(entity1), MessageEntity.class);
	}
	
	@Test
	public void testId() {
		Assertions.assertEquals(1L, entity1.getId());
	}
	
	@Test
	public void testAuthor() {
		Assertions.assertEquals("Author", entity1.getAuthor());
	}
	
	@Test
	public void testMessage() {
		Assertions.assertEquals("Message", entity1.getMessage());
	}
	
	@Test
	public void testToString() {
		Assertions.assertEquals("MessageEntity(id=1, author=Author, message=Message, question=null)", entity1.toString());
	}
	
	@Test
	public void testToDTO() {
		Assertions.assertTrue((new MessageDTO(1L, "Author", "Message")).equals(entity1.toMessageDTO()));
	}
	
	@Test
	public void testEquals() {
		Assertions.assertTrue(entity1.equals(entity1));
		Assertions.assertFalse(entity1.equals(new Object()));
		Assertions.assertTrue(entity1.equals(entity5));
		Assertions.assertFalse(entity1.equals(entity3));
		Assertions.assertFalse(entity1.equals(entity4));
	}
	
	@Test
	public void testHash() {
		Assertions.assertEquals(-1410603953, entity1.hashCode());
	}
}
