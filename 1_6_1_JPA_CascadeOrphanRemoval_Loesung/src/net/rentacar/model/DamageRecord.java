package net.rentacar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class DamageRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String description;

	@ManyToOne
	private RentalContract contract;

	public DamageRecord() {
	}

	public DamageRecord(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public RentalContract getContract() {
		return contract;
	}

	public void setContract(RentalContract contract) {
		this.contract = contract;
	}
}
