package com.codingshuttle.project.uber.uberApp.repositories;

import com.codingshuttle.project.uber.uberApp.entities.Driver;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {

	@Query(value = "Select d.*, ST_Distance(d.current_location, :pickupLocation) AS distance " +
			"FROM driver AS d where d.available = true AND ST_DWithin(d.current_location, :pickupLocation, 10000) " +
			"ORDER BY distance " +
			"LIMIT 10", nativeQuery = true
	)
	List<Driver> findTenNearestDrivers(Point pickupLocation);

	//List<Driver> findTenHighestRatedDrivers(Point pickupLocation);
}
