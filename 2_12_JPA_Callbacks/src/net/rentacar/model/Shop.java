package net.rentacar.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_Shop")
public class Shop extends AbstractBusinessObject{

	@Column(nullable = false, length = 100)
	private String location;
	@OneToMany(mappedBy="location")
	private Set<VehicleItem> Vehicles = new HashSet<VehicleItem>();

	public Shop() {
	}

	public Shop(String id, String location) {
		super(id);
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Set<VehicleItem> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Set<VehicleItem> Vehicles) {
		this.vehicles = Vehicles;
	}

	@Override
	public String toString() {
		return this.location;
	}


}
