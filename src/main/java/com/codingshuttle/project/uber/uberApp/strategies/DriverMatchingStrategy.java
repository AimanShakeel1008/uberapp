package com.codingshuttle.project.uber.uberApp.strategies;

import com.codingshuttle.project.uber.uberApp.entities.Driver;
import com.codingshuttle.project.uber.uberApp.entities.RideRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DriverMatchingStrategy {

	List<Driver> findMatchingDrivers(RideRequest rideRequest);
}
