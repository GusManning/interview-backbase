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
public class RegistrationEntity {
	public final static int NAME_LENGTH = 300;
	
	@Id
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Transient
	@EqualsAndHashCode.Exclude
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long courseId;
	
	@Column(name="name",nullable=false,length=NAME_LENGTH)
	private String name;
	
	@EqualsAndHashCode.Exclude
	@Column(name="registrationDate",nullable=false)
	private LocalDate registrationDate;
	
	@JsonSetter("cancelDate")
	public void setCancelDate(String date) {
		registrationDate = LocalDate.parse(date);
	}
	
	
	
}
