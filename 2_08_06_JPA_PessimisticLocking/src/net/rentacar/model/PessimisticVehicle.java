package net.rentacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PessimisticVehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String registrationNumber;
	private boolean reserved;

	public PessimisticVehicle() {
	}

	public PessimisticVehicle(String registrationNumber, boolean reserved) {
		this.registrationNumber = registrationNumber;
		this.reserved = reserved;
	}

	public Long getId() {
		return id;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
}
