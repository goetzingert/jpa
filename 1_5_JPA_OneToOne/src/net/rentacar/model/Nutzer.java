package net.rentacar.model;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name="tbl_Nutzer")
public class Nutzer implements Serializable {

	@Id
	@GeneratedValue
	private long id;
	private long kundennummer;

	@OneToOne(cascade = CascadeType.PERSIST)
	private Person person;

	public Person getPerson() {
		return person;
	}

	public long getId() {
		return id;
	}

	public Nutzer() {
		super();
	}

	public Nutzer( String firstName, String lastName) {
		this.person = new Person();
		this.person.setFirstName(firstName);
		this.person.setLastName(lastName);
	}

}
