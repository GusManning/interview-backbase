package com.backbase.q1;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Entity
@Table(name="courses")
@JsonPropertyOrder({"id", "title","startDate","endDate","capacity","remaining","participants"})
public class CourseEntity {
	
	public static final int TITLE_LENGTH = 255;
	

	@Id
	@EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="title",nullable=false,length=TITLE_LENGTH)
	private String title;
	@Column(name="start_date",nullable=false)
	private LocalDate startDate;
	@Column(name="end_date",nullable=false)
	private LocalDate endDate;
	@Column(name="capacity",nullable=false)
	private Integer capacity;
	
	@Transient
	@JsonIgnore
	private Integer spaceUsed;
	
	
    @JoinColumn(name = "course_id")
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RegistrationEntity> participants;
	
	@JsonGetter(value = "remaining")
	public Integer getRemaining() {
		if (participants != null) {
			return capacity - participants.size();
		} 
		
		if (spaceUsed != null) {
			return capacity - spaceUsed;
		}
		
		return capacity;
	}
	
	
}
