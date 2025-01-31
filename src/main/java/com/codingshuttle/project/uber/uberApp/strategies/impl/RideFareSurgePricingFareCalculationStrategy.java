package com.codingshuttle.project.uber.uberApp.strategies.impl;

import com.codingshuttle.project.uber.uberApp.entities.RideRequest;
import com.codingshuttle.project.uber.uberApp.services.DistanceService;
import com.codingshuttle.project.uber.uberApp.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {
	private final DistanceService distanceService;
	private static final double SURGE_FACTOR = 2;

	@Override
	public double calculateFare(RideRequest rideRequest) {
		double distance = distanceService.calculateDistance(rideRequest.getPickupLocation(),
				rideRequest.getDropOffLocation());

		log.info("distance:"+distance);

		return distance * RIDE_FARE_MULTIPLIER * SURGE_FACTOR;
	}

}
