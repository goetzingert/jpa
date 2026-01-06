package net.rentacar.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "tbl_Shop")
public class Shop extends AbstractBusinessObject {

	@Column(nullable = false, length = 100)
	private String location;
	@OneToMany(mappedBy = "location")
	@Fetch(FetchMode.SUBSELECT)
	@BatchSize(size=100)
	private Set<Vehicle> carpool = new HashSet<Vehicle>();

	public Shop() {
	}

	public Shop(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Set<Vehicle> getCarpool() {
		return carpool;
	}

	public void setCarpool(Set<Vehicle> Vehicles) {
		this.carpool = Vehicles;
	}

	@Override
	public String toString() {
		return this.location;
	}

}
