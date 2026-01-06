package net.rentacar.model;

public class Vehicle {

	private long id;
	private VehicleType type;
	private Shop location;

	public Vehicle( Shop location, VehicleType type) {
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
