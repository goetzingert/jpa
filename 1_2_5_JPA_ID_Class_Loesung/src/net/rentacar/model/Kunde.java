/**
 * 
 */
package net.rentacar.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

/**
 * @author Thomas G&ouml;tzinger
 * 
 */
@Entity
@IdClass(Nutzer.class)
public class Kunde {

	private String kundennummer;

	@Id
	private String firstName;
	@Id
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
		this.setKundennummer(UUID.randomUUID().toString());
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

	public void setKundennummer(String kundennummer) {
		this.kundennummer = kundennummer;
	}

	public String getKundennummer() {
		return kundennummer;
	}

}
