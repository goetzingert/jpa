package net.rentacar.model;

import java.io.Serializable;

public class Nutzer implements Serializable {

	public String firstName;
	public String lastName;

	public Nutzer() {
		super();
	}

	public Nutzer(String firstName, String lastName) {

		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Nutzer) {
			Nutzer other = (Nutzer) obj;
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
