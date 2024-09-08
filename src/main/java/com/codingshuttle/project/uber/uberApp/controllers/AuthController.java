package com.codingshuttle.project.uber.uberApp.controllers;

import com.codingshuttle.project.uber.uberApp.dto.*;
import com.codingshuttle.project.uber.uberApp.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	ResponseEntity<UserDto> signUp(@RequestBody SignupDto signupDto) {
		return new ResponseEntity<>(authService.signup(signupDto), HttpStatus.CREATED);
	}

	@PostMapping("/onBoardNewDriver/{userId}")
	ResponseEntity<DriverDto> onBoardNewDriver(@PathVariable Long userId, @RequestBody OnBoardDriverDto onBoardDriverDto) {

		return new ResponseEntity<>(authService.onBoardNewDriver(userId, onBoardDriverDto.getVehicleId()), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {

		String[] tokens = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

		Cookie cookie = new Cookie("token" , tokens[1]);
		cookie.setHttpOnly(true);

		httpServletResponse.addCookie(cookie);

		return ResponseEntity.ok(new LoginResponseDto(tokens[0]));
	}
}

