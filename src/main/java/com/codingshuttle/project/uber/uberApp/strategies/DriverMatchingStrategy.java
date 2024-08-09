package com.codingshuttle.project.uber.uberApp.strategies;

import com.codingshuttle.project.uber.uberApp.dto.RideRequestDto;
import com.codingshuttle.project.uber.uberApp.entities.Driver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DriverMatchingStrategy {

	List<Driver> findMatchingDriver(RideRequestDto rideRequestDto);
}
