package com.auth.service;

import org.springframework.stereotype.Service;

import com.auth.io.ProfileRequest;
import com.auth.io.ProfileResponse;

@Service
public interface ProfileService {
	
	public ProfileResponse createProfile(ProfileRequest request);
	public ProfileResponse getProfile(String email);
	
	public void sendResetOtp(String email);
	
	public void resetPassword(String email,String otp,String newPassword);
	
	public void sendOtp(String email);
	
	public void  verifyOtp(String email,String otp);
	
	
	
	

}
