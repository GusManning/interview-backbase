package com.backbase.q2;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EncoderService {
	// we sort our char map so we can use BinarySearch later
	
	static {
		char[] tmp = "ABCDEF0123456789".toCharArray();
		Arrays.sort(tmp);
		CHAR_MAP = tmp;
	}
	public static final char[] CHAR_MAP;
	public static final int BASE_ENCODING = CHAR_MAP.length;
	public static final Pattern ALLOWED_PATTERN = Pattern.compile("[^a-fA-F0-9]");
	
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
		StringBuilder shortId = new StringBuilder();
		
		while(hash > 0) {
			shortId.append(CHAR_MAP[hash % BASE_ENCODING]);
			hash = hash/BASE_ENCODING;
		}
		
		if (shortId.length() == 0) {
			shortId.append(CHAR_MAP[0]);
		}
		
		return shortId.reverse().toString();
	}
	
	/**
	 * Decodes a Hexadecimal string into a non-negative Hash.  Will throw a BAD_REQUEST if given a 
	 * argument which does not use Hexadecimal characters.
	 * @param shortId String of characters defined in CHAR_MAP.
	 * @return the original hash encoded by encode.
	 */
	public int decode(String shortId) {
		shortId = shortId.toUpperCase();
		if(ALLOWED_PATTERN.matcher(shortId).find()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, shortId + " is not a valid code ");
		}
		char[] shortChars = shortId.toCharArray();
		int hash = 0;
		int counter = 1;
		for(int i = 0; i < shortChars.length; i++) {
			hash += Arrays.binarySearch(CHAR_MAP, shortChars[i])*Math.pow(BASE_ENCODING, shortChars.length - counter);
			counter++;
		}
		
		return hash;
	}
}
