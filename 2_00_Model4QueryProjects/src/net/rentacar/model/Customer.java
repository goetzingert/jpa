/**
 * 
 */
package net.rentacar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

/**
 * @author Thomas G&ouml;tzinger
 * 
 */
@Entity
public class Customer extends User {

	private String customerReferenceNumber;

	@OneToMany(cascade = { CascadeType.PERSIST,CascadeType.MERGE })
	private List<Reservation> reservations = new ArrayList<Reservation>();

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Customer() {
		super();
		this.customerReferenceNumber = UUID.randomUUID().toString();
	}


	public Customer(Person p) {
		super(p);
		this.customerReferenceNumber = UUID.randomUUID().toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Customer) {
			Customer other = (Customer) obj;
			return this.customerReferenceNumber.equals(other.customerReferenceNumber);
		}
		return false;
	}

	public String getCustomerReferenceNumber() {
		return customerReferenceNumber;
	}

	public void setCustomerReferenceNumber(String customerReferenceNumber) {
		this.customerReferenceNumber = customerReferenceNumber;
	}

	@Override
	public int hashCode() {
		return customerReferenceNumber.hashCode();
	}

}
