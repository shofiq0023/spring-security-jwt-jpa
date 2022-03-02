package com.security.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.jwt.models.AuthenticationRequest;
import com.security.jwt.models.AuthenticationResponse;
import com.security.jwt.services.MyUserDetailsService;
import com.security.jwt.util.JwtUtil;

@RestController
public class UserController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@GetMapping("/")
	public String home() {
		return "hello";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	
	@GetMapping("/user")
	public String user() {
		return "user";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authReq) throws Exception {
		try {
			authManager.authenticate(
				new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword())	
			);
		} catch (Exception e) {
			throw new Exception("Incorrect username or password ", e);
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authReq.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
