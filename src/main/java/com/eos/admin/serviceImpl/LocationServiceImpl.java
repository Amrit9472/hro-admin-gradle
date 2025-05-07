package com.eos.admin.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.admin.entity.Location;
import com.eos.admin.repository.LocationRepository;
import com.eos.admin.service.LocationService;
@Service
public class LocationServiceImpl implements LocationService {


	private LocationRepository locationRepository;
	
	
    @Autowired
	public LocationServiceImpl(LocationRepository locationRepository) {
		super();
		this.locationRepository = locationRepository;
	}



	@Override
	public Location saveLocation(Location location) {

		return locationRepository.save(location);
	}

}
