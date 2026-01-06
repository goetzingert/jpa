package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.GregorianCalendar;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	private Vehicle vehicleItem;

	public void setUp() throws Exception {
		
		VehicleType Vehicle = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(Vehicle);
		VehicleType Vehicle2 = new Truck(new Model("Mercedes", "10to"), 120,
				200, 10000);
		manager.persist(Vehicle2);
		VehicleType Vehicle3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(Vehicle3);
		Shop shop = new Shop("Muenchen");
		vehicleItem = new Vehicle(shop, Vehicle);
		Shop stuttgart = new Shop("Stuttgart");
		vehicleItem.setLocation(stuttgart);
		manager.persist(vehicleItem);
		Vehicle vehicleItem2 = new Vehicle(stuttgart, Vehicle3);
		manager.persist(vehicleItem2);
		manager.persist(new Shop("Koeln"));
		Customer customer = new Customer(new Person("Hans", "Mustermann"));
		manager.persist(customer);
		Customer customer2 = new Customer(new Person("Franz", "Mueller"));
		manager.persist(customer2);
		manager.persist(new Customer(new Person("Herbert", "Schmitt")));
		manager.persist(new Customer(new Person("Ingo", "Meyer")));
		manager.persist(new Customer(new Person("Mathias", "Mayer")));
		manager.persist(new Customer(new Person("Michael", "Anst�dt")));
		manager.persist(new Customer(new Person("Ralf", "Gross")));
		this.createReservierung(customer, vehicleItem, stuttgart, 100,
				new GregorianCalendar(2007, 12, 18, 12, 0),
				new GregorianCalendar(2007, 12, 19, 12, 0));
		this.createReservierung(customer2, vehicleItem, shop, 110,
				new GregorianCalendar(2008, 11, 22, 12, 0),
				new GregorianCalendar(2008, 11, 23, 18, 0));
		this.createReservierung(customer, vehicleItem2, stuttgart, 220,
				new GregorianCalendar(2008, 07, 12, 12, 0),
				new GregorianCalendar(2008, 07, 14, 12, 0));
		this.createReservierung(customer2, vehicleItem2, stuttgart, 330,
				new GregorianCalendar(2007, 04, 30, 12, 0),
				new GregorianCalendar(2007, 05, 02, 12, 0));
		this.createReservierung(customer, vehicleItem2, shop, 440,
				new GregorianCalendar(2007, 10, 01, 12, 0),
				new GregorianCalendar(2007, 10, 04, 18, 0));
		manager.flush();
		manager.clear();
	}

	private void createReservierung(Customer customer, Vehicle vehicle,
									Shop shop, float preis, GregorianCalendar start,
									GregorianCalendar ende) {
		Reservation res = new Reservation(vehicle, shop, shop, start,
				ende, preis);
		customer.getReservations().add(res);
		manager.merge(customer);
	}

	
	@Test public void testSubQuery()
	{
		//TODO selektiere alle Kunden die mehr als 200 GE f¸r Reservierungen ausgegeben haben
	}

}
