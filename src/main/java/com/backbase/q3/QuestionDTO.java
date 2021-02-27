package com.backbase.q3;

import lombok.Value;

@Value
public class QuestionDTO {
	Long id;
	String author;
	String message;
	Integer replies;
}
