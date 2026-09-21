package net.rentacar.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class RentalContract {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String customer;

	// TODO: Konfiguriere @OneToMany mit mappedBy = "contract", cascade = CascadeType.ALL und orphanRemoval = true
	@OneToMany(mappedBy = "contract")
	private List<DamageRecord> damageRecords = new ArrayList<>();

	public RentalContract() {
	}

	public RentalContract(String customer) {
		this.customer = customer;
	}

	public Long getId() {
		return id;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public List<DamageRecord> getDamageRecords() {
		return damageRecords;
	}

	public void addDamageRecord(DamageRecord record) {
		damageRecords.add(record);
		record.setContract(this);
	}

	public void removeDamageRecord(DamageRecord record) {
		damageRecords.remove(record);
		record.setContract(null);
	}
}
