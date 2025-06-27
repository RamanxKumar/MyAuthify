package com.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.properties.mail.smtp.from}")
	private String fromEmail;
	
	
	public void sendWelcomeEmail(String toEmail,String name) {
		SimpleMailMessage message =new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Welcome to Our PlatForm");
		message.setText("Hello"+name+",\n\n Thanks For registering with us! \n\n Regards ,\n MyAuthify Team");
		mailSender.send(message);
		
		
	}
	
	public void SendResetOtpEmail(String toEmail,String otp) {
		SimpleMailMessage message =new SimpleMailMessage();
		
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Password Reset OTP!");
		message.setText("Your OTP for Reseting Your Password is "+otp+".Use this OTP to proceed with reseting the password!");
		
		mailSender.send(message);
	}
	
	public void sendOtpEmail(String toEmail,String otp) {
		SimpleMailMessage message =new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Account Verfication OTP");
		message.setText("Your OTP is "+otp+". Verify your account using this OTP.");
		mailSender.send(message);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
