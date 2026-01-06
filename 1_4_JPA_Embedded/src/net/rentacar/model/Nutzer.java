package net.rentacar.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_Nutzer")
public class Nutzer implements Serializable {

	@Id
	@GeneratedValue
	private long id;
	@Column(length = 50)
	private String firstName;
	@Column(length = 60)
	private String lastName;

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

	public long getId() {
		return id;
	}

	public Nutzer() {
		super();
	}

	public Nutzer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

}
