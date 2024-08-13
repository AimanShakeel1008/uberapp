package com.codingshuttle.project.uber.uberApp.services.impl;

import com.codingshuttle.project.uber.uberApp.services.DistanceService;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {
	@Override
	public double calculateDistance(Point source, Point destination) {
		//call the third party API OSRM to calculate distance
		return 0;
	}
}
