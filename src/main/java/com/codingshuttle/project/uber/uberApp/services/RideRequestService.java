package com.codingshuttle.project.uber.uberApp.services;

import com.codingshuttle.project.uber.uberApp.entities.RideRequest;
import org.springframework.stereotype.Service;

@Service
public interface RideRequestService {

	RideRequest findRideRequestById(Long rideRequestId);
	void update(RideRequest rideRequest);
}
