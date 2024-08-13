package com.codingshuttle.project.uber.uberApp.strategies;

import com.codingshuttle.project.uber.uberApp.dto.RideRequestDto;
import com.codingshuttle.project.uber.uberApp.entities.RideRequest;
import org.springframework.stereotype.Service;

@Service
public interface RideFareCalculationStrategy {

	double RIDE_FARE_MULTIPLIER = 10;
	double calculateFare(RideRequest rideRequest);
}
