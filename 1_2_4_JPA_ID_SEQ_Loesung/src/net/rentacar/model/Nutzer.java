package net.rentacar.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Nutzer implements Serializable {

	@SequenceGenerator(name = "Nutzer_SEQ", sequenceName = "Vehicle_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Nutzer_SEQ")
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
		return this.id;
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
