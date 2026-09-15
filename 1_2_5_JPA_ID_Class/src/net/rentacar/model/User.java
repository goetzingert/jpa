package net.rentacar.model;

import java.io.Serializable;

public class User implements Serializable {

	public String firstName;
	public String lastName;

	public User() {
		super();
	}

	public User(String firstName, String lastName) {

		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User other = (User) obj;
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
