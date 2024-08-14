package com.codingshuttle.project.uber.uberApp.services.impl;

import com.codingshuttle.project.uber.uberApp.dto.DriverDto;
import com.codingshuttle.project.uber.uberApp.dto.RideDto;
import com.codingshuttle.project.uber.uberApp.dto.RiderDto;
import com.codingshuttle.project.uber.uberApp.entities.Driver;
import com.codingshuttle.project.uber.uberApp.entities.Ride;
import com.codingshuttle.project.uber.uberApp.entities.RideRequest;
import com.codingshuttle.project.uber.uberApp.entities.enums.RideRequestStatus;
import com.codingshuttle.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.codingshuttle.project.uber.uberApp.repositories.DriverRepository;
import com.codingshuttle.project.uber.uberApp.services.DriverService;
import com.codingshuttle.project.uber.uberApp.services.RideRequestService;
import com.codingshuttle.project.uber.uberApp.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

	private final RideRequestService rideRequestService;
	private final DriverRepository driverRepository;

	private final ModelMapper modelMapper;

	private final RideService rideService;
	@Override
	@Transactional
	public RideDto acceptRide(Long rideRequestId) {

		RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);

		if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)) {
			throw new RuntimeException("Ride Request cannot be accepted, status is "+rideRequest.getRideRequestStatus());
		}

		Driver currentDriver = getCurrentDriver();

		if (!currentDriver.getAvailable()) {
			throw new RuntimeException("Driver cannot ride due to unavailability");
		}

		Ride ride = rideService.createNewRide(rideRequest, currentDriver);

		return modelMapper.map(ride, RideDto.class);
	}

	@Override
	public RideDto cancelRide(Long rideId) {
		return null;
	}

	@Override
	public RideDto startRide(Long rideId) {
		return null;
	}

	@Override
	public RideDto endRide(Long rideId) {
		return null;
	}

	@Override
	public RiderDto rateRider(Long rideId, Integer rating) {
		return null;
	}

	@Override
	public DriverDto getMyProfile() {
		return null;
	}

	@Override
	public List<RideDto> getAllMyRides() {
		return null;
	}

	@Override
	public Driver getCurrentDriver() {
		return driverRepository.findById(2L).orElseThrow(()-> new ResourceNotFoundException("Driver not found with id: "+2));
	}
}
