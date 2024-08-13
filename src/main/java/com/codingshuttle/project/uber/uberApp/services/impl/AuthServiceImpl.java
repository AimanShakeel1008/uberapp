package com.codingshuttle.project.uber.uberApp.services.impl;

import com.codingshuttle.project.uber.uberApp.dto.DriverDto;
import com.codingshuttle.project.uber.uberApp.dto.SignupDto;
import com.codingshuttle.project.uber.uberApp.dto.UserDto;
import com.codingshuttle.project.uber.uberApp.entities.User;
import com.codingshuttle.project.uber.uberApp.entities.enums.Role;
import com.codingshuttle.project.uber.uberApp.exceptions.RuntimeConflictException;
import com.codingshuttle.project.uber.uberApp.repositories.UserRepository;
import com.codingshuttle.project.uber.uberApp.services.AuthService;
import com.codingshuttle.project.uber.uberApp.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final ModelMapper modelMapper;
	private final UserRepository userRepository;

	private final RiderService riderService;
	@Override
	public String login(String email, String password) {
		return null;
	}

	@Override
	public UserDto signup(SignupDto signupDto) {

		User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);

		if (user != null) throw new RuntimeConflictException("Cannot signup, User already exists with email " +signupDto.getEmail());

		User mappedUser = modelMapper.map(signupDto, User.class);

		mappedUser.setRoles(Set.of(Role.RIDER));

		User savedUser = userRepository.save(mappedUser);

//      CREATE USER RELATED ENTITIES
//      1. RIDER
		riderService.createNewRider(savedUser);
//		2. TODO add WALLET related Services here



		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public DriverDto onboardNewDriver(Long userId) {
		return null;
	}
}
