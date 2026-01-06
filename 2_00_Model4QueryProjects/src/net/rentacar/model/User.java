package net.rentacar.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_Nutzer")
public class User extends AbstractBusinessObject {

	@OneToOne(cascade = CascadeType.PERSIST)
	private Person person;

	public User() {
		super();
	}

	public User(Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}


}
