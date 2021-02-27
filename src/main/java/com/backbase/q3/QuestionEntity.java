package com.backbase.q3;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
public class QuestionEntity extends MessageEntity {
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "questions", 
			joinColumns = {@JoinColumn(name = "root_message_id_fk")},
			inverseJoinColumns = {@JoinColumn(name = "reply_message_id_fk")}
	)	
	@Getter @Setter private List<MessageEntity> replies; 
	
	public QuestionDTO toDTO() {
		int replies = this.replies == null ? 0 : this.replies.size();
		return new QuestionDTO(getId(), getAuthor(), getMessage(), replies);
	}
}
