package net.rentacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "TBL_Car")
public class Car extends VehicleType {

	private int doors;

	public Car() {
	}


	public Car(Model model, long hp, long maxKpH, int doors) {
		super(model, hp, maxKpH);
		this.doors = doors;
	}


	public int getDoors() {
		return doors;
	}

	public void setDoors(int doors) {
		this.doors = doors;
	}

}
