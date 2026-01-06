package net.rentacar.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_Shop")
public class Shop {

	@Id
	private String id;
	@Column(nullable = false, length = 100)
	private String location;
	@OneToMany()
	@JoinColumn(name="shop_id")
	private Set<Vehicle> vehicles = new HashSet<Vehicle>();

	public Shop() {
	}

	public Shop(String id, String location) {
		this.id = id;
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
