package net.rentacar.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@NamedQueries({ @NamedQuery(name = "Reservierung.getAllReservationsBybrand", query = "SELECT res FROM Reservierung res WHERE res.Vehicle.type.model.brand =:brand") })
@Entity
@Table(name = "tbl_Reservation")
public class Reservierung extends AbstractBusinessObject {

	@ManyToOne(optional = false)
	private VehicleItem Vehicle;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startZeit;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar rueckgabeZeit;
	@ManyToOne(optional = false)
	private Shop startShop;
	@ManyToOne(optional = false)
	private Shop rueckgabeShop;
	private float preis;

	public Reservierung() {
	}

	public Reservierung(String id, VehicleItem VehicleItem,
			Shop startShop, Shop rueckgabeShop,
			GregorianCalendar start, GregorianCalendar ende, float preis) {
		super(id);
		setVehicle(VehicleItem);
		setStartShop(startShop);
		setRueckgabeShop(rueckgabeShop);
		setStartZeit(start);
		setRueckgabeZeit(ende);
		setPreis(preis);
	}

	public Reservierung(VehicleItem VehicleItem, Shop startShop,
			Shop rueckgabeShop, GregorianCalendar start,
			GregorianCalendar ende, float preis) {
		super();
		setVehicle(VehicleItem);
		setStartShop(startShop);
		setRueckgabeShop(rueckgabeShop);
		setStartZeit(start);
		setRueckgabeZeit(ende);
		setPreis(preis);
	}

	public float getPreis() {
		return preis;
	}

	public void setPreis(float preis) {
		this.preis = preis;
	}

	public VehicleItem getVehicle() {
		return Vehicle;
	}

	public void setVehicle(VehicleItem Vehicle) {
		this.Vehicle = Vehicle;
	}

	public Calendar getStartZeit() {
		return startZeit;
	}

	public void setStartZeit(Calendar startZeit) {
		this.startZeit = startZeit;
	}

	public Calendar getRueckgabeZeit() {
		return rueckgabeZeit;
	}

	public void setRueckgabeZeit(Calendar rueckgabeZeit) {
		this.rueckgabeZeit = rueckgabeZeit;
	}

	public Shop getStartShop() {
		return startShop;
	}

	public void setStartShop(Shop startShop) {
		this.startShop = startShop;
	}

	public Shop getRueckgabeShop() {
		return rueckgabeShop;
	}

	public void setRueckgabeShop(Shop rueckgabeShop) {
		this.rueckgabeShop = rueckgabeShop;
	}

}
