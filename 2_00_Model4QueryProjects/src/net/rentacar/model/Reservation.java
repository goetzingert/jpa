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

@Entity
@Table(name = "tbl_Reservation")
@NamedQueries({
	@NamedQuery(name= Reservation.FIND_BY_START_Shop, query= Reservation.FIND_BY_START_Shop)
})
public class Reservation extends AbstractBusinessObject {
	
	public static final String PARAM_Shop = "Shop";

	public static final String FIND_BY_START_Shop = "SELECT r FROM Reservation r WHERE r.startShop = :" + PARAM_Shop;

	@ManyToOne(optional = false)
	private Vehicle vehicle;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar returnTime;
	@ManyToOne(optional = false)
	private Shop startShop;
	@ManyToOne(optional = false)
	private Shop returnShop;
	private float price;

	public Reservation() {
	}

	public Reservation(Vehicle vehicleItem, Shop startShop,
					   Shop returnShop, GregorianCalendar start,
					   GregorianCalendar ende, float price) {
		super();
		setVehicle(vehicleItem);
		setStartShop(startShop);
		setReturnShop(returnShop);
		setStartTime(start);
		setReturnTime(ende);
		setPrice(price);
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startZeit) {
		this.startTime = startZeit;
	}

	public Calendar getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Calendar rueckgabeZeit) {
		this.returnTime = rueckgabeZeit;
	}

	public Shop getStartShop() {
		return startShop;
	}

	public void setStartShop(Shop startShop) {
		this.startShop = startShop;
	}

	public Shop getReturnShop() {
		return returnShop;
	}

	public void setReturnShop(Shop rueckgabeShop) {
		this.returnShop = rueckgabeShop;
	}

}
