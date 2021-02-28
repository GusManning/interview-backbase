package com.backbase.q3;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@SpringBootTest("ForumService.class")
public class ForumServiceTest {

	@MockBean
	public QuestionRepository qRepository;
	@MockBean
	public MessageRepository mRepository;
	@Autowired
	ForumService service;
	
	private QuestionEntity question;
	private MessageEntity message;
	private QuestionDTO dto;
	
	@BeforeEach
	public void setup() {
		question = new QuestionEntity();
		question.setId(1L);
		question.setAuthor("");
		question.setMessage("");
		
		message = new MessageEntity();
		dto = new QuestionDTO(1L, "", "", 0);
	}
	
	@Test
	public void testCreate() {
		question.setId(null);
		Mockito.when(qRepository.save(question)).thenReturn(question);
		Assertions.assertEquals(new QuestionDTO(null,"","",0), service.create(question));
	}
	
	@Test
	public void testFindAllQuestions() {
		Mockito.when(qRepository.findAll()).thenReturn(List.of(question));
		Assertions.assertEquals(List.of(dto), service.findAllQuestions());
	}
	
	@Test
	public void testFindQuestionsById() {
		Mockito.when(qRepository.findById(1L)).thenReturn(Optional.of(question));
		Assertions.assertEquals(question, service.findQuestionsById(1L));
	}
	
	@Test
	public void testAddReply() {
		Mockito.when(qRepository.findById(1L)).thenReturn(Optional.of(question));
		Mockito.when(mRepository.save(message)).thenReturn(message);
		Assertions.assertEquals(message, service.addReply(1L, message));
	}
	
	@Test
	public void failCreateQuestion() {
		HttpStatus status = null;
		try {
			service.create(question);
		} catch (ResponseStatusException rse) {
			status = rse.getStatus();
		}
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, status);
	}
	
	@Test
	public void failAddReply() {
		Mockito.when(qRepository.findById(1L)).thenReturn(Optional.empty());
		HttpStatus status = null;
		try {
			service.addReply(1L, message);
		} catch (ResponseStatusException rse) {
			status = rse.getStatus();
		}
		Assertions.assertEquals(HttpStatus.NOT_FOUND, status);
	}
	
	@Test
	public void failFindById() {
		Mockito.when(qRepository.findById(1L)).thenReturn(Optional.empty());
		HttpStatus status = null;
		try {
			service.findQuestionsById(1L);
		} catch (ResponseStatusException rse) {
			status = rse.getStatus();
		}
		Assertions.assertEquals(HttpStatus.NOT_FOUND, status);
	}
	
}
