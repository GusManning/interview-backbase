package com.backbase.q3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionEntityTest {
	private QuestionEntity entity1;

	
	@BeforeEach
	public void setup() throws IOException {
		entity1 = new QuestionEntity();
		entity1.setId(1L);
		entity1.setAuthor("Author");
		entity1.setMessage("Message");
		entity1.setMessages(new ArrayList<MessageEntity>());
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
	public void testMessages() {
		Assertions.assertEquals(List.of(), entity1.getMessages() );
	}
	
	@Test
	public void testReplies() {
		MessageEntity mEntity = new MessageEntity();
		mEntity.setId(1L);
		entity1.setMessages(List.of(mEntity));
		Assertions.assertEquals(mEntity.getId(), entity1.getReplies().get(0).getId());
	}
	
	@Test
	public void testToDTO() {
		Assertions.assertTrue((new QuestionDTO(1L, "Author", "Message", 0)).equals(entity1.toQuestionDTO()));
		entity1.getMessages().add(new MessageEntity());
		Assertions.assertEquals(1, entity1.toQuestionDTO().getReplies());
	}
	
	@Test
	public void testHash() {
		Assertions.assertEquals(-1410603953, entity1.hashCode());
	}
	
}
