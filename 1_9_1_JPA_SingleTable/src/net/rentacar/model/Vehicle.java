package net.rentacar.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_Vehicle")
public class Vehicle {

	@Id
	@GeneratedValue
	private long id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private VehicleType type;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Shop location;
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "tbl_Vehicle_2_location", joinColumns = { @JoinColumn(name = "vehicle_id") }, inverseJoinColumns = { @JoinColumn(name = "shop_id") })
	private List<Shop> locationHistory = new ArrayList<Shop>();

	public Vehicle() {
	}

	public Vehicle(Shop location, VehicleType type) {
		setLocation(location);
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setType(VehicleType type) {
		this.type = type;
	}

	public VehicleType getType() {
		return type;
	}

	public void setLocation(Shop location) {
		location.getVehicles().add(this);
		if (this.location != null) {
			this.locationHistory.add(this.location);
			this.location.getVehicles().remove(this);
		}
		this.location = location;

	}

	public Shop getLocation() {
		return location;
	}

	public List<Shop> getLocationHistory() {
		return locationHistory;
	}
}
