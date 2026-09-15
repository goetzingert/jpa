package net.rentacar.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ComparableVehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String brand;
	private String modell;

	public ComparableVehicle() {
	}

	public ComparableVehicle(String brand, String modell) {
		this.brand = brand;
		this.modell = modell;
	}

	public ComparableVehicle(Long id, String brand, String modell) {
		this.id = id;
		this.brand = brand;
		this.modell = modell;
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

	public String getModell() {
		return modell;
	}

	public void setModell(String modell) {
		this.modell = modell;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ComparableVehicle that = (ComparableVehicle) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
