package com.security.jwt.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.jwt.models.MyUserDetails;
import com.security.jwt.models.UserModel;
import com.security.jwt.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserModel> user = userRepo.findByUsername(username);
		user.orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
		
		return user.map(MyUserDetails::new).get();
	}
	
}
