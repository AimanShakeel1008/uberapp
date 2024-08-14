package com.codingshuttle.project.uber.uberApp.controllers;

import com.codingshuttle.project.uber.uberApp.dto.RideDto;
import com.codingshuttle.project.uber.uberApp.services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

	private final DriverService driverService;

	@PostMapping("/acceptRide/{rideRequestId}")
	public RideDto acceptRide(@PathVariable Long rideRequestId) {
		return driverService.acceptRide(rideRequestId);
	}
}
