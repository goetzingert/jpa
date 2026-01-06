package net.rentacar.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_Nutzer")
public class Nutzer extends AbstractBusinessObject {

	@OneToOne(cascade = CascadeType.PERSIST)
	@PrimaryKeyJoinColumn
	private Person person;

	public Nutzer() {
		super();
	}

	public Nutzer(String id, Person person) {
		super(id);
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}


}
