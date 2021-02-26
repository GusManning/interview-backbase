package com.backbase;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StubRegistry {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long courseId;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String cancelDate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String registrationDate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;
}
