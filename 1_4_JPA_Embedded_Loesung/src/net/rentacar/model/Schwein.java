package net.rentacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Schwein {
	@Id
	@GeneratedValue
	private long id;

	private String name;

	private int gewicht;

	@OneToOne()
	private Bauer besitzer;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGewicht() {
		return gewicht;
	}

	public void setGewicht(int gewicht) {
		this.gewicht = gewicht;
	}

	public Bauer getBesitzer() {
		return besitzer;
	}

	public void setBesitzer(Bauer bauer) {
		this.besitzer = bauer;
	}
}
