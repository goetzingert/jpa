package net.rentacar.model;

public class Truck extends VehicleType {

	private int maxLoad;

	public Truck() {
		super();
	}

	public Truck( Model model, long hp, long maxKph, int maxLoad) {
		super( model, hp, maxKph);
		this.maxLoad = maxLoad;
	}

	public int getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(int maxLoad) {
		this.maxLoad = maxLoad;
	}
}
