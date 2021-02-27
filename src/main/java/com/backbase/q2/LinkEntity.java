package com.backbase.q2;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Data
@Entity
@Table(name="links")
/**
 * LinkEntity composes the short and long versions of a link, using a hash of the long version of the link as an ID.
 * A database created timestamp is used for record removal.
 */
public class LinkEntity {
	@Id
	private Integer id;
	/**
	 * The tiny version of the link.
	 */
	private String shortLink;
	/**
	 * full version of a link.
	 */
	private String longLink;
	@UpdateTimestamp
	/**
	 * timestamp used to find old records on the database.
	 */
	private Timestamp createdOn;
}
