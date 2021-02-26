package com.backbase.q1;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name="registrations")
/**
 * The RegistrationEntity is used for both additions and removals of students from CourseEntity objects. 
 * Lombok has added a Equals method which allows quick comparison when removing students as ArrayList uses
 * equals to match List elements. 
 *
 */
public class RegistrationEntity {
	public final static int NAME_LENGTH = 300;
	
	@Id
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.AUTO)
	/**
	 * Primary key for Registration
	 */
	private Long id;
	
	@Transient
	@EqualsAndHashCode.Exclude
	@JsonInclude(JsonInclude.Include.NON_NULL)
	/**
	 * Transient courseId, used when first adding or removing and is replaced with a foreign key when persisted.
	 */
	private Long courseId;
	
	@Column(name="name",nullable=false,length=NAME_LENGTH)
	private String name;
	
	@EqualsAndHashCode.Exclude
	@Column(name="registrationDate",nullable=false)
	private LocalDate registrationDate;
	
	@JsonSetter("cancelDate")
	/**
	 * This allows cancelDate to be parsed into RegistrationDate allowing RegistrationEntity 
	 * to serve as both cancelation and registration object.
	 * @param date string date that will be parsed into LocalDate.
	 */
	public void setCancelDate(String date) {
		registrationDate = LocalDate.parse(date);
	}
	
	
	
}
