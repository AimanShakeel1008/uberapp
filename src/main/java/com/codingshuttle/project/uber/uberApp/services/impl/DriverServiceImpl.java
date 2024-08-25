package com.codingshuttle.project.uber.uberApp.services.impl;

import com.codingshuttle.project.uber.uberApp.dto.DriverDto;
import com.codingshuttle.project.uber.uberApp.dto.RideDto;
import com.codingshuttle.project.uber.uberApp.dto.RiderDto;
import com.codingshuttle.project.uber.uberApp.entities.Driver;
import com.codingshuttle.project.uber.uberApp.entities.Ride;
import com.codingshuttle.project.uber.uberApp.entities.RideRequest;
import com.codingshuttle.project.uber.uberApp.entities.enums.RideRequestStatus;
import com.codingshuttle.project.uber.uberApp.entities.enums.RideStatus;
import com.codingshuttle.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.codingshuttle.project.uber.uberApp.repositories.DriverRepository;
import com.codingshuttle.project.uber.uberApp.services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

	private final RideRequestService rideRequestService;
	private final DriverRepository driverRepository;

	private final ModelMapper modelMapper;

	private final RideService rideService;

	private final PaymentService paymentService;
	private final RatingService ratingService;

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

		Driver savedDriver = updateDriverAvailability(currentDriver, false);

		Ride ride = rideService.createNewRide(rideRequest, savedDriver);

		return modelMapper.map(ride, RideDto.class);
	}

	@Override
	@Transactional
	public RideDto cancelRide(Long rideId) {
		Ride ride = rideService.getRideById(rideId);
		Driver currentDriver = getCurrentDriver();

		if (!currentDriver.equals(ride.getDriver())) {
			throw new RuntimeException("Driver cannot cancel the ride as he has not accepted it earlier");
		}

		if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
			throw new RuntimeException("Ride status is not confirmed, hence cannot be cancelled, invalid status: "+ride.getRideStatus());
		}

		rideService.updateRideStatus(ride,RideStatus.CANCELLED);

		updateDriverAvailability(currentDriver,true);

		return modelMapper.map(ride, RideDto.class);
	}

	@Override
	@Transactional
	public RideDto startRide(Long rideId, String otp) {
		Ride ride = rideService.getRideById(rideId);
		Driver driver = getCurrentDriver();

		if (!driver.equals(ride.getDriver())) {
			throw new RuntimeException("Driver cannot start the ride as he has not accepted it earlier");
		}

		if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
			throw new RuntimeException("Ride status is not confirmed, hence cannot be started, status: "+ride.getRideStatus());
		}

		if (!otp.equals(ride.getOtp())) {
			throw new RuntimeException("OTP is not valid.");
		}

		ride.setStartedAt(LocalDateTime.now());

		Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);

		paymentService.createNewPayment(savedRide);

		ratingService.createNweRating(savedRide);

		return modelMapper.map(savedRide, RideDto.class);
	}

	@Override
	@Transactional
	public RideDto endRide(Long rideId) {

		Ride ride = rideService.getRideById(rideId);
		Driver driver = getCurrentDriver();

		if (!driver.equals(ride.getDriver())) {
			throw new RuntimeException("Driver cannot end the ride as he has not accepted it earlier");
		}

		if (!ride.getRideStatus().equals(RideStatus.ONGOING)) {
			throw new RuntimeException("Ride status is not ONGOING, hence cannot be ended, status: "+ride.getRideStatus());
		}

		ride.setEndedAt(LocalDateTime.now());

		Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);
		updateDriverAvailability(driver,true);
		paymentService.processPayment(ride);

		return modelMapper.map(savedRide,RideDto.class);
	}

	@Override
	public RiderDto rateRider(Long rideId, Integer rating) {

		Ride ride = rideService.getRideById(rideId);
		Driver driver = getCurrentDriver();

		if (!driver.equals(ride.getDriver())) {
			throw new RuntimeException("Driver is not the owner of this ride.");
		}

		if (!ride.getRideStatus().equals(RideStatus.ENDED)) {
			throw new RuntimeException("Ride status is not ENDED, hence cannot start rating, status: "+ride.getRideStatus());
		}

		return ratingService.rateRider(ride, rating);
	}

	@Override
	public DriverDto getMyProfile() {
		Driver currentDriver = getCurrentDriver();
		return modelMapper.map(currentDriver, DriverDto.class);
	}

	@Override
	public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
		Driver currentDriver = getCurrentDriver();

		return rideService.getAllRidesOfDriver(currentDriver,pageRequest).map(
				ride -> modelMapper.map(ride, RideDto.class)
		);
	}

	@Override
	public Driver getCurrentDriver() {
		return driverRepository.findById(2L).orElseThrow(()-> new ResourceNotFoundException("Driver not found with id: "+2));
	}

	@Override
	public Driver updateDriverAvailability(Driver driver, boolean available) {

		driver.setAvailable(available);

		return driverRepository.save(driver);
	}

	@Override
	public Driver createNewDriver(Driver driver) {
		return driverRepository.save(driver);
	}
}
