package com.codingshuttle.project.uber.uberApp.controllers;

import com.codingshuttle.project.uber.uberApp.dto.RideDto;
import com.codingshuttle.project.uber.uberApp.dto.RideStartDto;
import com.codingshuttle.project.uber.uberApp.services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

	private final DriverService driverService;

	@PostMapping("/acceptRide/{rideRequestId}")
	public RideDto acceptRide(@PathVariable Long rideRequestId) {
		return driverService.acceptRide(rideRequestId);
	}

	@PostMapping("/startRide/{rideId}")
	public RideDto startRide(@PathVariable Long rideId, @RequestBody RideStartDto rideStartDto) {
		return driverService.startRide(rideId, rideStartDto.getOtp());
	}

	@PostMapping("/endRide/{rideId}")
	public RideDto endRide(@PathVariable Long rideId) {
		return driverService.endRide(rideId);
	}
}
