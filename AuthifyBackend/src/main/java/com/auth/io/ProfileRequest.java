package com.auth.io;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {
	
	@NotBlank(message="Name should not be Empty!!")
	private String name;
	@Email(message="Enter Valid Email Address!!")
	@NotNull(message="Email Should Not be Empty!!")
	private String email;
	@Size(min=6,message="Password must be atleast of 6 characters!!")
	private String password;
	

}
