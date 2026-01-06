package net.rentacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Stall {

	@Id
	@GeneratedValue
	private long id;

	@OneToMany
	private List<Schwein> bewohner = new ArrayList<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Schwein> getBewohner() {
		return bewohner;
	}

	public void setBewohner(List<Schwein> bewohner) {
		this.bewohner = bewohner;
	}
}
