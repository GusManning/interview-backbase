package com.backbase.q2;

import java.sql.Timestamp;
import java.time.Instant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkEntityTest {

	private LinkEntity link; 
	private Timestamp stamp;
	
	@BeforeEach
	public void setup() {
		stamp = Timestamp.from(Instant.now());
		link = new LinkEntity();
		link.setCreatedOn(stamp);
		link.setId(1);
		link.setLongLink("long link");
		link.setShortLink("short");
	}
	
	@Test
	public void testDate() {
		Assertions.assertEquals(stamp, link.getCreatedOn());
	}
	
	@Test
	public void testId() {
		Assertions.assertEquals(1, link.getId());
	}
	
	@Test
	public void testLongLink() {
		Assertions.assertEquals("long link", link.getLongLink());
	}
	
	@Test
	public void testShortLink() {
		Assertions.assertEquals("short", link.getShortLink());
	}
	
	@Test
	public void testHashCode() {
		Assertions.assertEquals(69699417, link.hashCode());
	}
	
	@Test
	public void testEquals() {
		LinkEntity link2 = new LinkEntity();
		link.setLongLink(link.getLongLink());
		Assertions.assertFalse(link.equals(link2));
		Assertions.assertFalse(link.equals(new Object()));
		Assertions.assertTrue(link.equals(link));
	}
	
}
