package net.rentacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BulkVehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String brand;
	private double dailyRate;
	private boolean active;

	public BulkVehicle() {
	}

	public BulkVehicle(String brand, double dailyRate, boolean active) {
		this.brand = brand;
		this.dailyRate = dailyRate;
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getDailyRate() {
		return dailyRate;
	}

	public void setDailyRate(double dailyRate) {
		this.dailyRate = dailyRate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
