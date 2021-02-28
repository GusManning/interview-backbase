package com.backbase.q3;

import lombok.Value;

@Value
/**
 * MessageDTO is a data transfer object for message entity.  Its serialization is designed to match the expected
 * output of this application as listed in Q3.  It is used when returning a QuestionEntity as the list of attached
 * Messages do not show all fields of MessageEntity.
 */
public class MessageDTO {
	Long id;
	String author;
	String message;
}
