package com.devtest.devtest.service;

public interface SecurityService {

	String createToken(String subject,long ttlMillis);
	String getSubject(String token);
	Boolean jwtExpireCheck(int time);
	
}
