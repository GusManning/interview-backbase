package com.backbase;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class StubCourse {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Getter @Setter private String title;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Getter @Setter private String startDate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Getter @Setter private String endDate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Getter @Setter private Integer capacity;
}
