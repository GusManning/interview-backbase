package com.backbase.q2;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@Service
public class LinkService {

	@Autowired
	private LinkRepository repository;
	
	@Autowired
	private EncoderService encoder;
	
	/**
	 * Takes a url and hashes it, uses that hash to store the full version and passes back a string code used by
	 * tinyToLong to get the full version of the Url.
	 * @param longUrl a properly formed Url, will throww a BAD_REQUEST if given anything else.
	 * @return String shortened version of a URL
	 */
	public String longToTiny(String longUrl) {
		// this will catch poorly formatted urls as well as ones over 2000 characters, etc
		try {
			new URL(longUrl);
		} catch (MalformedURLException mue) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"URL '" + longUrl + "' is not valid");
		}
		int hash = Math.abs(longUrl.hashCode());
		String shortUrl = encoder.encode(hash);
		
		LinkEntity entity = new LinkEntity();
		entity.setId(hash);
		entity.setShortLink(shortUrl);
		entity.setLongLink(longUrl);
		
		// if you want to make it so that calling longToTiny resets the timer then just save
		if(repository.findById(entity.getId()).isEmpty()) {
			repository.save(entity);
		}
		
		return shortUrl;
	}
	
	/**
	 * Turns a code string back into a full Url by unpacking the hash and finding it the the database.
	 * @param tiny
	 * @return
	 */
	public String tinyToLong(String tiny) {	
		LinkEntity link = repository.findById(encoder.decode(tiny))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"URL not found in DB"));
		
		return link.getLongLink();
	}
	
	/**
	 * This will delete all records older than a specific time
	 * @param time a LocalDateTime 
	 */
	public void deleteRecordsOlderThan(LocalDateTime time) {
		Timestamp timestamp = Timestamp.valueOf(time);
		repository.deleteByCreatedOnBefore(timestamp);
	}
	

	@Scheduled(fixedDelay = 60000, initialDelay = 60000)
	/**
	 * Runs every minute to delete any LinkEntity old than 30 minutes.
	 */
	public void deleteOlderThanHalfHour() {
		deleteRecordsOlderThan(LocalDateTime.now().minusMinutes(30));
	}

	/**
	 * Test method I created to check on timestamps of links to make sure they were being deleted.
	 * @return List of all LinkEntities
	 */
	
	public List<LinkEntity> findAllLinks() {
		return repository.findAll();
	}

	
}
