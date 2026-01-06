package net.rentacar.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Car")
public class Car extends VehicleType {

	private int doors;

	public Car() {
	}
	
	public Car(String id, Model model, long hp, long maxKph, int doors) {
		super(id, model, hp, maxKph);
		this.doors = doors;
	}

	public int getDoors() {
		return doors;
	}

	public void setDoors(int doors) {
		this.doors = doors;
	}

}
