package com.backbase.q3;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class ForumService {

	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private MessageRepository messageRepository;

	
	public QuestionEntity createOrUpdate(QuestionEntity entity) {
		return questionRepository.save(entity);
	}
	
	public MessageEntity createOrUpdate(MessageEntity entity) {
		return messageRepository.save(entity);
	}
	
	public List<QuestionEntity> findAllQuestions() {
		return questionRepository.findAll();
	}
	
	public QuestionEntity findQuestionsById(Long id) {
		QuestionEntity entity = questionRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Question " + id + " not found"));
		System.out.println(entity.getReplies().toString());
		return entity;
	}

	public MessageEntity addReply(Long questionId, MessageEntity message) {
		QuestionEntity question = questionRepository.findById(questionId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Question " + questionId + " not found"));
		message.setQuestion(question);
		return messageRepository.save(message);
	}

}
