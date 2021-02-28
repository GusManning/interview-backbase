package com.backbase.q2;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EncoderService {
	
	/**
	 * Turns a hash into an short string, only works on non-negative numbers so you will need to use Math.abs on 
	 * any has you generate as there is a good chance it might be negative. 
	 * @param hash Any non negative integer
	 * @return a short string in Hexadecimal.
	 */
	public String encode(int hash) {
		if(hash < 0) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Server encountered an internal error");
		}
		return Integer.toHexString(hash).toUpperCase();
	}
	
	/**
	 * Decodes a Hexadecimal string into a non-negative Hash.  Will throw a BAD_REQUEST if given a 
	 * argument which does not use Hexadecimal characters.
	 * @param shortId String of characters in hex
	 * @return the original hash encoded by encode.
	 */
	public int decode(String shortId) {
		if(shortId.length() % 2 == 1) {
			shortId = "0x0" + shortId;
		} else {
			shortId = "0x" + shortId;
		}
		try { 
			return Integer.decode(shortId);
		} catch (NumberFormatException nfe) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, shortId + " is not a valid code ");
		}
	}
}
