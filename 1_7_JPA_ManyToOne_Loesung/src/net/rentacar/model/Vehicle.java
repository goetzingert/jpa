package net.rentacar.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_Vehicle")
public class Vehicle {

	@Id
	private String id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private VehicleType type;
	@ManyToOne()
	private Shop location;

	public Vehicle() {
	}

	public Vehicle(String id, Shop location, VehicleType type) {
		this.id = id;
		setLocation(location);
		this.type = type;
	}

	public void setType(VehicleType type) {
		this.type = type;
	}

	public VehicleType getType() {
		return type;
	}

	public void setLocation(Shop location) {
		if (this.location != null)
			this.location.getVehicles().remove(this);
		this.location = location;
		if (location != null) {
			location.addVehicle(this);
		}
	}

	public Shop getLocation() {
		return location;
	}
}
