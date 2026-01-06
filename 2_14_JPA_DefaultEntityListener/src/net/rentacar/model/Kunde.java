/**
 * 
 */
package net.rentacar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

/**
 * @author Thomas G&ouml;tzinger
 * 
 */
@Entity
public class Kunde extends Nutzer {

	private String kundennummer;

	@OneToMany(cascade = { CascadeType.PERSIST,CascadeType.MERGE })
	private List<Reservierung> reservierungen = new ArrayList<Reservierung>();

	public List<Reservierung> getReservierungen() {
		return reservierungen;
	}

	public void setReservierungen(List<Reservierung> reservierungen) {
		this.reservierungen = reservierungen;
	}

	public Kunde() {
		super();
		this.kundennummer = UUID.randomUUID().toString();
	}

	/**
	 * Konstruktor der Klasse
	 * 
	 * @param p
	 *            Personeninformation des Kunden
	 */
	public Kunde(String id, Person p) {
		super(id, p);
		this.kundennummer = UUID.randomUUID().toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Kunde) {
			Kunde other = (Kunde) obj;
			return this.kundennummer.equals(other.kundennummer);
		}
		return false;
	}

	public String getKundennummer() {
		return kundennummer;
	}

	public void setKundennummer(String kundennummer) {
		this.kundennummer = kundennummer;
	}

	@Override
	public int hashCode() {
		return kundennummer.hashCode();
	}

}
