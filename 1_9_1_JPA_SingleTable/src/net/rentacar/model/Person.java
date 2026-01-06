package net.rentacar.model;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * Entity implementation class for Entity: Person
 * 
 */
@Entity
@Table(name="tbl_person")
public class Person implements Serializable {

	@Id
	@GeneratedValue
	private long id;
	@Column(length = 50)
	private String firstName;
	@Column(length = 60)
	private String lastName;

	public Person() {
		super();
	}
	

	public Person(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
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

	public long getId() {
		return id;
	}


}
