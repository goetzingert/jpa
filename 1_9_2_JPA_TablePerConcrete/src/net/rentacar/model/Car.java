package net.rentacar.model;

import jakarta.persistence.Entity;

@Entity
public class Car extends VehicleType {

	private int doors;

	public Car() {
	}

	public Car(Model model, long hp, long maxKph, int doors) {
		super( model, hp, maxKph);
		this.doors = doors;
	}

	public int getDoors() {
		return doors;
	}

	public void setDoors(int doors) {
		this.doors = doors;
	}

}
