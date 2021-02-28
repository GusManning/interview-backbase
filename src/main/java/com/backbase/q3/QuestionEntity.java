package com.backbase.q3;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity

/**
 * QuestionEntity is the child class of MessageEntity and is the root of a Question > Reply chain.  
 * It contains a OneToMany relation with it's own parent class.  
 */
public class QuestionEntity extends MessageEntity {
	
	// if QuestionEnity is going to be updateable these columns should be have insertable and updatable set to false.
	@OneToMany
	@JsonIgnore
	@JoinTable(name = "question_replys", 
			joinColumns = {@JoinColumn(name = "root_question_id_fk", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "reply_message_id_fk", referencedColumnName = "id")}
	)	
	@Getter @Setter private List<MessageEntity> messages; 
	
	/**
	 * Converts this entity into a Data Transfer Object which shows a much more limited amount of information
	 * when serialzied.
	 * @return a DTO of this entity.
	 */
	public QuestionDTO toQuestionDTO() {
		int replies = this.messages == null ? 0 : this.messages.size();
		return new QuestionDTO(getId(), getAuthor(), getMessage(), replies);
	}
	
	@JsonGetter("replies")
	/**
	 * JsonGetter for replies, rather than using MessageEntity objects which have more information than
	 * is expected we convert all MessageEntity objects on serialization to DTOs. 
	 * @return List of DTOs
	 */
	public List<MessageDTO> getReplies() {
		return messages.stream().map((m)->{return m.toMessageDTO();}).collect(Collectors.toList());
	}
	
}
