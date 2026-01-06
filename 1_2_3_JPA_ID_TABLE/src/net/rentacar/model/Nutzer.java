package net.rentacar.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Nutzer implements Serializable {

	@Id
	private Long id;
	private String firstName;
	private String lastName;

	public Nutzer() {
		super();
	}

	public Nutzer(String firstName, String lastName) {

		this.setFirstName(firstName);
		this.setLastName(lastName);
	}

	public Long getId() {
		return id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

}
