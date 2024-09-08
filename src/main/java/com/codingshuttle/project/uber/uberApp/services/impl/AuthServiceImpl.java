package com.codingshuttle.project.uber.uberApp.services.impl;

import com.codingshuttle.project.uber.uberApp.dto.DriverDto;
import com.codingshuttle.project.uber.uberApp.dto.SignupDto;
import com.codingshuttle.project.uber.uberApp.dto.UserDto;
import com.codingshuttle.project.uber.uberApp.entities.Driver;
import com.codingshuttle.project.uber.uberApp.entities.User;
import com.codingshuttle.project.uber.uberApp.entities.enums.Role;
import com.codingshuttle.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.codingshuttle.project.uber.uberApp.exceptions.RuntimeConflictException;
import com.codingshuttle.project.uber.uberApp.repositories.UserRepository;
import com.codingshuttle.project.uber.uberApp.security.JWTService;
import com.codingshuttle.project.uber.uberApp.services.AuthService;
import com.codingshuttle.project.uber.uberApp.services.DriverService;
import com.codingshuttle.project.uber.uberApp.services.RiderService;
import com.codingshuttle.project.uber.uberApp.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final ModelMapper modelMapper;
	private final UserRepository userRepository;

	private final RiderService riderService;
	private final WalletService walletService;

	private final DriverService driverService;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	private final JWTService jwtService;
	@Override
	public String[] login(String email, String password) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, password)
		);

		User user = (User)authentication.getPrincipal();

		String accessToken = jwtService.generateAccessToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);

		return new String[]{accessToken, refreshToken};
	}

	@Override
	@Transactional
	public UserDto signup(SignupDto signupDto) {

		User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);

		if (user != null) throw new RuntimeConflictException("Cannot signup, User already exists with email " +signupDto.getEmail());

		User mappedUser = modelMapper.map(signupDto, User.class);

		mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));

		mappedUser.setRoles(Set.of(Role.RIDER));

		User savedUser = userRepository.save(mappedUser);

//      CREATE USER RELATED ENTITIES
//      1. RIDER
		riderService.createNewRider(savedUser);

		walletService.crateNewWallet(savedUser);

		return modelMapper.map(savedUser, UserDto.class);
	}


	@Override
	public DriverDto onBoardNewDriver(Long userId, String vehicleId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with Id: "+userId));

		if(user.getRoles().contains(Role.DRIVER)) throw new RuntimeConflictException("User with id "+userId + "is already a Driver");

		Driver createDriver = Driver.builder()
				.user(user)
				.rating(0.0)
				.vehicleId(vehicleId)
				.available(true)
				.build();
		user.getRoles().add(Role.DRIVER);

		userRepository.save(user);

		Driver createdDriver = driverService.createNewDriver(createDriver);

		return modelMapper.map(createdDriver, DriverDto.class);
	}
}
