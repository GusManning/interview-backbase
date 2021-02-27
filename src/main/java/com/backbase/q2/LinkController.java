package com.backbase.q2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinkController {
	
	@Autowired
	private LinkService service;
	
	@GetMapping("/short")
	public ResponseEntity<String> linkLongToShort(@RequestParam(name="url", required = true) String url) {
		return new ResponseEntity<String>(service.longToTiny(url),new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/long")
	public ResponseEntity<String> linkShortToLong(@RequestParam(name="tiny", required = true) String tiny) {
		return new ResponseEntity<String>(service.tinyToLong(tiny),new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/test")
	public ResponseEntity<List<LinkEntity>> showAllLinks() {
		return new ResponseEntity<List<LinkEntity>>(service.findAllLinks(),new HttpHeaders(), HttpStatus.OK);
	}
}
