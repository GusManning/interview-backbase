package com.backbase.q3;

import lombok.Value;

@Value
/**
 * QuestionDTO is a data transfer object used in place of QuestionEntity.  It's serialization omits a list
 * of MessageEntity objects or MessageDTOs for a number of replies under this question.
 */
public class QuestionDTO {
	Long id;
	String author;
	String message;
	Integer replies;
}
