package com.backbase.q3;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class MessageEntity {
	@Id
	@GeneratedValue
	@Column(name = "id")
	protected Long id;
	@NotNull
	@NotBlank
	private String author;
	@NotNull
	private String message;
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinTable(name = "questions", 
	joinColumns = {@JoinColumn(name = "root_message_id_fk")},
	inverseJoinColumns = {@JoinColumn(name = "reply_message_id_fk")}
	)	
	@JsonIgnore
	private QuestionEntity question;
	
	@JsonGetter("questionId")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Long getQuestionId() {
		if(getQuestion() == null) {
			return null;
		} else {
			return getQuestion().getId();
		}
	}
}
