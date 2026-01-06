package net.rentacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Vehicle {

	@Id
	@GeneratedValue
	private long id;
	//TODO n:1 unidrektional
	private VehicleType type;
	//TODO n:1 bidirektional
	private Shop location;

	public Vehicle() {
	}


	public Vehicle( Shop location, VehicleType type) {
		this.location = location;
		this.type = type;
	}
	
	public long getId() {
		return id;
	}

	public void setType(VehicleType type) {
		this.type = type;
	}

	public VehicleType getType() {
		return type;
	}

	public void setLocation(Shop location) {
		this.location = location;
	}

	public Shop getLocation() {
		return location;
	}
}
