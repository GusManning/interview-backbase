package com.backbase.q3;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class ForumController  {

	@Autowired
	private ForumService service;
	
	@PostMapping
	public ResponseEntity<QuestionDTO> postQuestion(@RequestBody QuestionEntity question) {
		return new ResponseEntity<QuestionDTO>(service.create(question), new HttpHeaders(), HttpStatus.CREATED);
	}
	
	@PostMapping("/{questionId}/reply")
	public ResponseEntity<MessageEntity> postReply(@PathVariable("questionId") Long questionId, @RequestBody MessageEntity message) {
		return new ResponseEntity<MessageEntity>(service.addReply(questionId, message), new HttpHeaders(), HttpStatus.CREATED);
	}
	
	@GetMapping("/{questionId}")
	public ResponseEntity<QuestionEntity> getThread(@PathVariable("questionId") Long questionId) {
		return new ResponseEntity<QuestionEntity>(service.findQuestionsById(questionId), new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
		return new ResponseEntity<List<QuestionDTO>>(service.findAllQuestions(), new HttpHeaders(), HttpStatus.OK);
	}
	

	
}
