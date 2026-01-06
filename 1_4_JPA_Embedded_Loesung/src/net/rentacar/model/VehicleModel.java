package net.rentacar.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class VehicleModel {

	@Embedded
	private Model model;
	private String series;

	public VehicleModel() {
	}

	public VehicleModel(String series, Model model) {
		this.series = series;
		this.model = model;
	}

	public Model getModel() {
		return model;
	}

	public String getSeries() {
		return series;
	}
}
