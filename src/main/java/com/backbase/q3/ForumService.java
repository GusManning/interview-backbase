package com.backbase.q3;

import java.util.List;
import java.util.stream.Collectors;

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

	/**
	 * Persists a new QuestionEntity, throws a 404 exception if the question has already been initialized.
	 * @param entity 
	 * @return Returns a QuestionDTO showing only the information we want to display to the user.
	 */
	public QuestionDTO create(QuestionEntity entity) {
		if(entity.getId() != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User cannot define Question id");
		}
		return questionRepository.save(entity).toQuestionDTO();
	}
	
	/**
	 * Returns a list of all Questions.  Since the listing is expected to be a shorter version of findQuestionById we
	 * return a list of DTOs which do not show all the MessageEntity objects under this question.
	 * @return
	 */
	public List<QuestionDTO> findAllQuestions() {
		return questionRepository.findAll().stream().map((q)->{return q.toQuestionDTO();}).collect(Collectors.toList());
	}
	
	/**
	 * Returns a QuestionEntity which can be shows not only the information associated with the Question but
	 * all Messages attached to it.
	 * @param id of QuestionEntity to return.
	 * @return
	 */
	public QuestionEntity findQuestionsById(Long id) {
		return questionRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Question " + id + " not found"));
	}

	/**
	 * Adds a Reply to a Question. Will throw a 404 if no QuestionEntity can be found for questionId.
	 * This function also persists the relationship between the reply and the Question so that when the
	 * QuestionEntity is requested it will have this Reply in its internal array.
	 * @param questionId
	 * @param message
	 * @return Returns a MessageEntity with its ID set.
	 */
	public MessageEntity addReply(Long questionId, MessageEntity message) {
		QuestionEntity question = questionRepository.findById(questionId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Question " + questionId + " not found"));
		message.setQuestion(question);
		return messageRepository.save(message);
	}

}
