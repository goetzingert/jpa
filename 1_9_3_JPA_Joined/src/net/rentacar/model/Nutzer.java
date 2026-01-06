package net.rentacar.model;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_Nutzer")
public class Nutzer implements Serializable {

	@Id
	@GeneratedValue
	private long id;
	@OneToOne(cascade = CascadeType.PERSIST)
	@PrimaryKeyJoinColumn
	private Person person;

	public Nutzer() {
		super();
	}

	public Nutzer( Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public long getId() {
		return id;
	}

}
