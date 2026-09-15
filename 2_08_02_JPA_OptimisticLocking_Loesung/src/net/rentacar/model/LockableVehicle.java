package net.rentacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@Entity
public class LockableVehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String brand;

	@Version
	private long version;

	public LockableVehicle() {
	}

	public LockableVehicle(String brand) {
		this.brand = brand;
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

	public long getVersion() {
		return version;
	}
}
