package com.codingshuttle.project.uber.uberApp.services;

import com.codingshuttle.project.uber.uberApp.dto.DriverDto;
import com.codingshuttle.project.uber.uberApp.dto.RideDto;
import com.codingshuttle.project.uber.uberApp.dto.RiderDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DriverService {

	RideDto acceptRide(Long rideId);

	RideDto cancelRide(Long rideId);

	RideDto startRide(Long rideId);

	RideDto endRide(Long rideId);

	RiderDto rateRider(Long rideId, Integer rating);

	DriverDto getMyProfile();

	List<RideDto> getAllMyRides();

}
