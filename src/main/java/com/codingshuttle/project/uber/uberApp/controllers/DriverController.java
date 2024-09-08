package com.codingshuttle.project.uber.uberApp.controllers;

import com.codingshuttle.project.uber.uberApp.dto.*;
import com.codingshuttle.project.uber.uberApp.services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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

	@PostMapping("/cancelRide/{rideId}")
	public RideDto cancelRide(@PathVariable Long rideId) {
		return driverService.cancelRide(rideId);
	}

	@GetMapping("/getMyProfile")
	public ResponseEntity<DriverDto> getMyProfile() {

		return ResponseEntity.ok(driverService.getMyProfile());
	}

	@GetMapping("/getMyRides")
	public ResponseEntity<Page<RideDto>> getMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
													@RequestParam(defaultValue = "10", required = false) Integer pageSize) {

		return ResponseEntity.ok(driverService.getAllMyRides(PageRequest.of(pageOffset, pageSize, Sort.by(Sort.Direction.DESC, "createdTime", "id"))));
	}

	@PostMapping("/rateRider")
	public ResponseEntity<RiderDto> rateRider(@RequestBody RatingDto ratingDto) {
		return ResponseEntity.ok(driverService.rateRider(ratingDto.getRideId(), ratingDto.getRating()));
	}
}
