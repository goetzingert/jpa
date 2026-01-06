package net.rentacar.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "tbl_Shop")
public class Shop {

	@Id
	@GeneratedValue
	private long id;
	@Column(nullable = false, length = 100)
	private String location;
	@Transient
	private Set<Vehicle> vehicles;

	public Shop() {
	}

	public Shop( String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Set<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Set<Vehicle> Vehicles) {
		this.vehicles = Vehicles;
	}

	@Override
	public String toString() {
		return this.location;
	}

	public Object getId() {
		return this.id;
	}

}
