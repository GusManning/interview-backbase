package com.backbase.q1;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

	/**
	 * @param infix String snippet that will be in the title of desired CourseEntity objects, case-sensitive.
	 * @return List of all CourseEntity objects with infix in title.
	 */
	List<CourseEntity> findByTitleContaining(String infix);
	
}
