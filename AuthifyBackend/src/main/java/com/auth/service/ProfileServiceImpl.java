package com.auth.service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.auth.entity.UserEntity;
import com.auth.io.ProfileRequest;
import com.auth.io.ProfileResponse;
import com.auth.repository.UserRepository;
import  com.auth.service.EmailService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor

public class ProfileServiceImpl implements ProfileService{
	
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	@Override
	public ProfileResponse createProfile(ProfileRequest request) {
		UserEntity newProfile = convertToUserEntity(request);
		if(!userRepository.existsByEmail(request.getEmail())) {
			newProfile = userRepository.save(newProfile);
			return convertToPofileResponse(newProfile);
		}
		
		throw new ResponseStatusException(HttpStatus.CONFLICT,"Email Already Exists");
		
		
	}

	private ProfileResponse convertToPofileResponse(UserEntity newProfile) {
		return ProfileResponse.builder()
				.name(newProfile.getName())
				.email(newProfile.getEmail())
				.userId(newProfile.getUserId())
				.isAccountVerified(newProfile.isAccountVerified())
				.build();
				
		
	}

	private UserEntity convertToUserEntity(ProfileRequest request) {
		return UserEntity.builder()
		.email(request.getEmail())
		.userId(UUID.randomUUID().toString())
		.name(request.getName())
		.password(passwordEncoder.encode(request.getPassword()))
		.isAccountVerified(false)
		.resetOtpExpireAt(0L)
		.verifyOtp(null)
		.verifyOtpExpireAt(0L)
		.resetOtp(null)
		.build();
		
	}

	@Override
	public ProfileResponse getProfile(String email) {
		UserEntity existingUser= userRepository.findByEmail(email)
		.orElseThrow(()-> new UsernameNotFoundException("User not Found"+email));
		return convertToPofileResponse(existingUser);
	}

	@Override
	public void sendResetOtp(String email) {
		 UserEntity existingEntity = userRepository.findByEmail(email)
				 .orElseThrow(()-> new UsernameNotFoundException("User Not Found!"+email));
		
		 //Generate 6 Digit Otp
		 String otp= String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
		 
		 //Calculate Expiry Time
		 
		 Long expiryTime= System.currentTimeMillis()+(15*60*1000);
		 
		 //Update The Profile/User
		 
		 existingEntity.setResetOtp(otp);
		 existingEntity.setResetOtpExpireAt(expiryTime);
		 
		 //Save into the database
		 
		 userRepository.save(existingEntity);
		 
		 try {
			 emailService.SendResetOtpEmail(existingEntity.getEmail(),otp);
		 } catch(Exception ex) {
			 throw new RuntimeException("Unable to send Email!!");
		 }
		 
	}

	@Override
	public void resetPassword(String email, String otp, String newPassword) {
		UserEntity existingUser = userRepository.findByEmail(email)
				 .orElseThrow(()-> new UsernameNotFoundException("User Not Found!"+email));
		
		if(existingUser.getResetOtp()==null || !existingUser.getResetOtp().equals(otp)){
			
			throw new RuntimeException("Invalid OTP!!");
		}
		
		if(existingUser.getResetOtpExpireAt() < System.currentTimeMillis()) {
			throw new RuntimeException("OTP Expired!!");
		}
		existingUser.setPassword(passwordEncoder.encode(newPassword));
		existingUser.setResetOtp(null);
		existingUser.setResetOtpExpireAt(0L);
		
		userRepository.save(existingUser);
		
	}




	@Override
	public void sendOtp(String email) {
		UserEntity existingUser = userRepository.findByEmail(email)
				 .orElseThrow(()-> new UsernameNotFoundException("User Not Found!"+email));
		
		if(existingUser.isAccountVerified() && existingUser.isAccountVerified()!=false) {
			return;
		}
		
		//Generate 6 Digit OTP
		 String otp= String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
		 
        //Calculate Expiry Time
		 
		 Long expiryTime= System.currentTimeMillis()+(24*60*60*1000);
		 
		//Update the User entity
		 
		 existingUser.setVerifyOtp(otp);
		 existingUser.setVerifyOtpExpireAt(expiryTime);
		 
		 //Save into the database
		 userRepository.save(existingUser);
		 
		 try {
			 emailService.sendOtpEmail(existingUser.getEmail(), otp); 
		 } catch(Exception ex) {
			 throw new RuntimeException("Unable to send otp on email!");
		 }
		 
		 
	}

	@Override
	public void verifyOtp(String email, String otp) {
	    UserEntity existingUser = userRepository.findByEmail(email)
	        .orElseThrow(() -> new UsernameNotFoundException("User Not Found! " + email));

	    // Check if OTP is null or does not match
	    if (existingUser.getVerifyOtp() == null || !existingUser.getVerifyOtp().equals(otp)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP!!");
	    }

	    // Check expiry
	    if (existingUser.getVerifyOtpExpireAt() < System.currentTimeMillis()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP Expired!!");
	    }

	    existingUser.setAccountVerified(true);
	    existingUser.setVerifyOtp(null);
	    existingUser.setVerifyOtpExpireAt(0L);

	    userRepository.save(existingUser);
	}
	
	
}
