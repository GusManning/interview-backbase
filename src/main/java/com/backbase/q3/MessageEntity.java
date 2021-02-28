package com.backbase.q3;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@Entity
@Table(name = "messages")
@JsonPropertyOrder({"questionId", "id","author","message"})
/**
 * MessageEntity is the parent class of QuestionEntity and store the message and author information for both
 * replys and questions.  It is also used to persist the link between QuestionEntity objects and MessageEntity 
 * Objects.
 */
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
	
	// the following is one half of the bidirectional OneToMany-ManyToOne relationship between QuestionEntity 
	// and MessageEntity
	@ManyToOne
	@JoinTable(name = "question_replys", 
		joinColumns = {@JoinColumn(name = "reply_message_id_fk", referencedColumnName = "id")},
		inverseJoinColumns = {@JoinColumn(name = "root_question_id_fk", referencedColumnName = "id")}
    )	
	@JsonIgnore
	private QuestionEntity question;
	
	@JsonGetter("questionId")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	/**
	 * Question Id is a JsonGetter used to present the QuestionId of a Message when Question is not null.
	 * @return question id if question is not null, null if otherwise.
	 */
	public Long getQuestionId() {
		if(getQuestion() == null) {
			return null;
		} else {
			return getQuestion().getId();
		}
	}
	
	/**
	 * Converts this message to a data transfer object which only shows a limited selection of information when
	 * serialized. 
	 * @return a MessageDTO of this object.
	 */
	public MessageDTO toMessageDTO() {
		return new MessageDTO(id, author, message);
	}
}
