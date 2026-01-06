package net.rentacar.model;

import java.util.Set;

public class Shop {

	private long id;
	private String location;
	private Set<Vehicle> vehicles;
	
	public Shop() {
	}
	
	public Shop( String location)
	{
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Set<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Set<Vehicle> Vehicles) {
		this.vehicles = Vehicles;
	}
	
	@Override
	public String toString() {
		return this.location;
	}

}
