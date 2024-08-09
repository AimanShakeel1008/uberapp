package com.codingshuttle.project.uber.uberApp.strategies;

import com.codingshuttle.project.uber.uberApp.dto.RideRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface RideFareCalculationStrategy {

	double calculateFare(RideRequestDto rideRequestDto);
}
