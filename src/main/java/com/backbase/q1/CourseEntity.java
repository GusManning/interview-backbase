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
/**
 * CourseEntity represents a Course composed of a title, start and ending dates.  It aggregates RegistrationEntity objects for
 * students currently enrolled in the course.  
 */
public class CourseEntity {
	
	public static final int TITLE_LENGTH = 255;
	

	@Id
	@EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.AUTO)
	/**
	 * Primary key for CourseEntity, should only be set by the database
	 */
	private Long id;
	
	@Column(name="title",nullable=false,length=TITLE_LENGTH)
	private String title;
	
	/**
	 * Start and End dates for the course.
	 */
	@Column(name="start_date",nullable=false)
	private LocalDate startDate;
	@Column(name="end_date",nullable=false)
	private LocalDate endDate;
	
	@Column(name="capacity",nullable=false)
	/**
	 * Max number of students in this course.
	 */
	private Integer capacity;
	
	@Transient
	@JsonIgnore
	/**
	 * Placeholder for returning remaining spaces if Participants is not included.  This field is not persisted on save
	 * and is only added to Json files via the getRemaining Json setter.
	 */
	private Integer spaceUsed;
	
	
    @JoinColumn(name = "course_id")
	@JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    /**
     * List of participants which is removed before serialization of any query that can return more than one CourseEntity.
     * RegistrationEntity objects are persisted to the database through CourseEntity as part of a One to Many relationship.
     */
	private List<RegistrationEntity> participants;
	
	@JsonGetter(value = "remaining")
	/**
	 * Ensures remaining Json field will be populated even if participants is not set.
	 * @return space remaining in this course
	 */
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
