package net.rentacar.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Booking {

	@EmbeddedId
	private BookingId id;

	private String customerName;
	private double totalAmount;

	public Booking() {
	}

	public Booking(BookingId id, String customerName, double totalAmount) {
		this.id = id;
		this.customerName = customerName;
		this.totalAmount = totalAmount;
	}

	public BookingId getId() {
		return id;
	}

	public void setId(BookingId id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
}
