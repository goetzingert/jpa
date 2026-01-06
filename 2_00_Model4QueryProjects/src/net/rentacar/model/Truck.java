package net.rentacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "TBL_TRUCK")
public class Truck extends VehicleType {

	private int maxLoad;

	public Truck() {
		super();
	}

	

	public Truck(Model model, long hp, long maxKpH, int maxLoad) {
		super(model, hp, maxKpH);
		this.maxLoad = maxLoad;
	}

	public int getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(int maxLoad) {
		this.maxLoad = maxLoad;
	}
}
