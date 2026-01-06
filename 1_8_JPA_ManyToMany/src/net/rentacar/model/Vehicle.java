package net.rentacar.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_Vehicle")
public class Vehicle {

	@Id
	@GeneratedValue
	private long id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private VehicleType type;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Shop location;

	public Vehicle() {
	}

	public Vehicle(Shop location, VehicleType type) {
		setLocation(location);
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
		this.location.getVehicles().add(this);
	}

	public Shop getLocation() {
		return location;
	}
}
