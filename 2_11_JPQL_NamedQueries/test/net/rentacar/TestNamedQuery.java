package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.GregorianCalendar;
import java.util.List;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestNamedQuery extends AbstractJPATestCase {

	private Vehicle vehicle;
	private Shop muenchen;

	@Override
	public void setUp() throws Exception {

		VehicleType Vehicle = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(Vehicle);
		VehicleType Vehicle2 = new Truck(new Model("Mercedes", "10to"), 120,
				200, 10000);
		manager.persist(Vehicle2);
		VehicleType Vehicle3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(Vehicle3);
		muenchen = new Shop("Muenchen");
		vehicle = new Vehicle(muenchen, Vehicle);
		Shop stuttgart = new Shop("Stuttgart");
		vehicle.setLocation(stuttgart);
		manager.persist(vehicle);
		Vehicle vehicle2 = new Vehicle(stuttgart, Vehicle3);
		manager.persist(vehicle2);
		manager.persist(new Shop("Koeln"));
		Customer customer = new Customer(new Person("Hans", "Mustermann"));
		manager.persist(customer);
		Customer customer2 = new Customer(new Person("Franz", "Mueller"));
		manager.persist(customer2);
		manager.persist(new Customer(new Person("Herbert", "Schmitt")));
		manager.persist(new Customer(new Person("Ingo", "Meyer")));
		manager.persist(new Customer(new Person("Mathias", "Mayer")));
		manager.persist(new Customer(new Person("Michael", "Anstaedt")));
		manager.persist(new Customer(new Person("Ralf", "Gross")));
		this.createReservierung(customer, vehicle, stuttgart, 100,
				new GregorianCalendar(2007, 12, 18, 12, 0),
				new GregorianCalendar(2007, 12, 19, 12, 0));
		this.createReservierung(customer2, vehicle, muenchen, 110,
				new GregorianCalendar(2008, 11, 22, 12, 0),
				new GregorianCalendar(2008, 11, 23, 18, 0));
		this.createReservierung(customer, vehicle2, stuttgart, 220,
				new GregorianCalendar(2008, 07, 12, 12, 0),
				new GregorianCalendar(2008, 07, 14, 12, 0));
		this.createReservierung(customer2, vehicle2, stuttgart, 330,
				new GregorianCalendar(2007, 04, 30, 12, 0),
				new GregorianCalendar(2007, 05, 02, 12, 0));
		this.createReservierung(customer, vehicle2, muenchen, 440,
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

	@Test
	public void testCallNamedQuery() {
		// 1. Einfacher Aufruf einer @NamedQuery über Konstantennamen
		Query namedQuery = null; // TODO: manager.createNamedQuery(Reservation.FIND_BY_START_Shop) und Parameter PARAM_Shop setzen
		assertNotNull(namedQuery);
		assertEquals(2, namedQuery.getResultList().size());
	}

	@Test
	public void testTypedNamedQuery() {
		// 2. Typsichere Ausführung mit TypedQuery<Reservation>
		TypedQuery<Reservation> typedQuery = null; // TODO: manager.createNamedQuery(Reservation.FIND_BY_START_Shop, Reservation.class)
		assertNotNull(typedQuery);

		List<Reservation> reservations = typedQuery.getResultList();
		assertEquals(2, reservations.size());
		assertNotNull(reservations.get(0).getVehicle());
	}

	@Test
	public void testNamedQueryWithMultipleParameters() {
		// 3. Named Query mit mehreren Parametern (Standort und Mindestpreis)
		List<Reservation> expensiveMuenchenReservations = null; // TODO: createNamedQuery(Reservation.FIND_BY_START_SHOP_AND_MIN_PRICE, Reservation.class) mit PARAM_Shop = muenchen und PARAM_MIN_PRICE = 200.0f
		assertNotNull(expensiveMuenchenReservations);
		assertEquals(1, expensiveMuenchenReservations.size());
		assertEquals(440.0f, expensiveMuenchenReservations.get(0).getPrice(), 0.01f);
	}

	@Test
	public void testNamedQueryScalarCount() {
		// 4. Skalare Zählabfrage via Named Query
		Long count = null; // TODO: manager.createNamedQuery(Reservation.COUNT_BY_START_SHOP, Long.class) mit PARAM_Shop = muenchen
		assertNotNull(count);
		assertEquals(2L, count);
	}

	@Test
	public void testNamedQueryDefinedInOrmXml() {
		// 5. Externe Named Query aus orm.xml aufrufen (ohne Code-Änderung an Entities)
		List<Shop> emptyShops = null; // TODO: manager.createNamedQuery("Shop.findWithNoVehicles", Shop.class)
		assertNotNull(emptyShops);
		assertEquals(2, emptyShops.size());
	}

	@Test
	public void testUndefinedNamedQueryThrowsException() {
		// 6. Randfall: Nicht existierende Named Query löst IllegalArgumentException aus
		assertThrows(IllegalArgumentException.class, () -> {
			manager.createNamedQuery("Reservation.nonExistentQuery");
		});
	}

}
