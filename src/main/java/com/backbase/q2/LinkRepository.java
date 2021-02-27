package com.backbase.q2;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<LinkEntity, Integer> {

	public void deleteByCreatedOnBefore(Timestamp expiryDate);

	public List<LinkEntity> findAllByCreatedOnBefore(Timestamp timestamp);
	
}
