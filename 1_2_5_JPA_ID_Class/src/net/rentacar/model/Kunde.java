/**
 * 
 */
package net.rentacar.model;

import java.util.UUID;

/**
 * @author Thomas G&ouml;tzinger
 * 
 */
public class Kunde {

	public String kundennummer;

	private String firstName;
	private String lastName;

	public Kunde() {
		this("", "");
	}

	/**
	 * Konstruktor der Klasse
	 * 
	 * @param firstName
	 * @param lastName
	 */
	public Kunde(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.kundennummer = UUID.randomUUID().toString();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Kunde) {
			Kunde other = (Kunde) obj;
			if (this.lastName.equals(other.lastName))
				return this.firstName.equals(other.firstName);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return lastName.hashCode() + firstName.hashCode();
	}

}
