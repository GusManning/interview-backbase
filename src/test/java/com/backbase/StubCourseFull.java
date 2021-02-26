package com.backbase;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class StubCourseFull extends StubCourse{
	
	@Getter @Setter private List<StubRegistry> participants;
	@Getter @Setter private Long id;
	@Getter @Setter private Integer remaining;
	
}
