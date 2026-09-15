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
	private String id;
	@Column(length = 50)
	private String firstName;
	@Column(length = 60)
	private String lastName;

	public Person() {
		super();
	}
	

	public Person(String id, String firstName, String lastName) {
		super();
		this.id = id;
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

	public String getId() {
		return id;
	}


}
