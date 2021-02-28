package com.backbase.q2;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name="links")
/**
 * LinkEntity composes the short and long versions of a link, using a hash of the long version of the link as an ID.
 * A database created timestamp is used for record removal.
 */
public class LinkEntity {
	// I could have probably replaced the getter for this with a reference to HashCode
	// I feel like that was what the question wanted as this was intended to be a test of Lombok
	// knowledge I just opted not to do that as I felt it wasn't best practice.
	@Id
	@EqualsAndHashCode.Exclude
	private Integer id;
	/**
	 * The tiny version of the link.
	 */
	@EqualsAndHashCode.Exclude
	private String shortLink;
	/**
	 * full version of a link.
	 */
	private String longLink;
	@UpdateTimestamp
	/**
	 * timestamp used to find old records on the database.
	 */
	@EqualsAndHashCode.Exclude
	private Timestamp createdOn;
}
