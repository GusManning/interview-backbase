package com.backbase.q2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EncoderServiceTest {

	EncoderService service;
	
	@BeforeEach
	public void setup() {
		service = new EncoderService();
	}
	
	@Test
	public void encodingDecodingTest() {
		int hash = Math.abs("ThisIsATestString".hashCode());
		String encoded = service.encode(hash);
		int hash2 = service.decode(encoded);
		
		Assertions.assertEquals(hash, hash2);
	}
	
	
	@Test
	public void encodingFail() {
		HttpStatus status = null;
		try {
			service.encode(-1);
		} catch (ResponseStatusException rse) {
			status = rse.getStatus();
		}
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status);
	}
	
	@Test
	public void decodingFail() {
		HttpStatus status = null;
		try {
			service.decode("ZYG@^&");
		} catch (ResponseStatusException rse) {
			status = rse.getStatus();
		}
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, status);
	}
}
