package net.rentacar.model;

import jakarta.persistence.*;

/**
 * Entity implementation class for Entity: Person
 * 
 */
@Entity
@Table(name="tbl_person")
public class Person extends AbstractBusinessObject {

	@Column(length = 50)
	private String firstName;
	@Column(length = 60)
	private String lastName;

	public Person() {
		super();
	}
	

	public Person(String id, String firstName, String lastName) {
		super(id);
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

}
