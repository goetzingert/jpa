package net.rentacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "tbl_Vehicle")
public class Vehicle {

	@Id
	private String id;
	@Transient
	private VehicleType type;
	@Transient
	private Shop location;
	
	public Vehicle() {
	
	}

	public Vehicle(String id, Shop location, VehicleType type) {
		this.id = id;
		this.location = location;
		this.type = type;
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
