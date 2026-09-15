package net.rentacar.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_User")
public class User implements Serializable {

	@SequenceGenerator(name = "User_SEQ", sequenceName = "Vehicle_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_SEQ")
	private Long id;
	private String firstName;
	private String lastName;

	public User() {
		super();
	}

	public User(String firstName, String lastName) {

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
