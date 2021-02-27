package com.backbase.q2;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
public class LinkServiceTest {

	@Autowired
	LinkService service;
	
	@MockBean
	LinkRepository repository;
	@MockBean
	EncoderService encoder;
	
	@Test
	public void testLongToTiny() {
		Mockito.when(encoder.encode(Mockito.anyInt())).thenReturn("I'm Encoded");
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		
		Assertions.assertEquals("I'm Encoded",service.longToTiny("http://www.google.ca"));
		Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());
		
		Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(new LinkEntity()));
		service.longToTiny("http://www.google.ca");
		Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());		
	}
	
	@Test
	public void testTinyToLong() {
		LinkEntity entity = new LinkEntity();
		entity.setLongLink("I'm a url");
		Mockito.when(repository.findById(1)).thenReturn(Optional.of(entity));
		Mockito.when(encoder.decode("small")).thenReturn(1);
		
		Assertions.assertEquals("I'm a url",service.tinyToLong("small"));
	}
	
	@Test
	public void failLongToTiny() {
		HttpStatus status = null;
		try {
			service.longToTiny("THIS IS NOT A VALID URL");
		} catch (ResponseStatusException rse) {
			status = rse.getStatus();
		}
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, status);
	}
	
	
	@Test
	public void failTinyToLong() {
		HttpStatus status = null;
		Mockito.when(repository.findById(1)).thenReturn(Optional.empty());
		Mockito.when(encoder.decode("small")).thenReturn(1);
		try {
			service.tinyToLong("won't find me");
		} catch (ResponseStatusException rse) {
			status = rse.getStatus();
		}
		Assertions.assertEquals(HttpStatus.NOT_FOUND, status);
	}
}
