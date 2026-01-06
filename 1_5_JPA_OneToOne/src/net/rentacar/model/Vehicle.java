package net.rentacar.model;

public class Vehicle {

	private String id;
	private VehicleType type;
	private Shop location;

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
