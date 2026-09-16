package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.GregorianCalendar;
import java.util.List;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	private Vehicle vehicle;

	public void setUp() throws Exception {
		VehicleType Vehicle = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(Vehicle);
		VehicleType Vehicle2 = new Truck(new Model("Mercedes", "10to"), 120,
				200, 10000);
		manager.persist(Vehicle2);
		VehicleType Vehicle3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(Vehicle3);
		Shop shop = new Shop("Muenchen");
		vehicle = new Vehicle(shop, Vehicle);
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
		manager.persist(new Customer(new Person("Michael", "Anst�dt")));
		manager.persist(new Customer(new Person("Ralf", "Gross")));
		this.createReservierung(customer, vehicle, stuttgart, 100,
				new GregorianCalendar(2007, 12, 18, 12, 0),
				new GregorianCalendar(2007, 12, 19, 12, 0));
		this.createReservierung(customer2, vehicle, shop, 110,
				new GregorianCalendar(2008, 11, 22, 12, 0),
				new GregorianCalendar(2008, 11, 23, 18, 0));
		this.createReservierung(customer, vehicle2, stuttgart, 220,
				new GregorianCalendar(2008, 07, 12, 12, 0),
				new GregorianCalendar(2008, 07, 14, 12, 0));
		this.createReservierung(customer2, vehicle2, stuttgart, 330,
				new GregorianCalendar(2007, 04, 30, 12, 0),
				new GregorianCalendar(2007, 05, 02, 12, 0));
		this.createReservierung(customer, vehicle2, shop, 440,
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
	public void testSubQuery() {
		// 1. Korrelierte Subquery in WHERE: Kunden mit Reservierungssumme > 200
		List<Customer> resultList = manager
				.createQuery(
						"SELECT k FROM Customer k WHERE (SELECT SUM(res.price) FROM k.reservations res) > 200 ORDER BY k.person.lastName", Customer.class)
				.getResultList();

		assertEquals(2, resultList.size());
		assertEquals("Mueller", resultList.get(0).getPerson().getLastName());
		assertEquals("Mustermann", resultList.get(1).getPerson().getLastName());
	}

	@Test
	public void returnsNoCustomerAboveAnUnreachableTotal() {
		// 2. Subquery mit unerreichbarem Schwellenwert: Liefert leere Liste
		List<Customer> resultList = manager.createQuery(
				"SELECT k FROM Customer k WHERE (SELECT SUM(res.price) FROM k.reservations res) > 1000", Customer.class)
				.getResultList();

		assertTrue(resultList.isEmpty());
	}

	@Test
	public void testSubqueryWithExistsOperator() {
		// 3. EXISTS-Operator: Finde alle Kunden, die mindestens eine hochpreisige Reservierung (>= 400 GE) haben
		List<Customer> highSpenders = manager.createQuery(
				"SELECT k FROM Customer k WHERE EXISTS (SELECT res FROM k.reservations res WHERE res.price >= 400)", Customer.class)
				.getResultList();

		assertEquals(1, highSpenders.size());
		assertEquals("Mustermann", highSpenders.get(0).getPerson().getLastName());
	}

	@Test
	public void testSubqueryWithNotExistsOperator() {
		// 4. NOT EXISTS-Operator: Finde alle Kunden ohne jegliche Reservierung
		List<Customer> inactiveCustomers = manager.createQuery(
				"SELECT k FROM Customer k WHERE NOT EXISTS (SELECT res FROM k.reservations res)", Customer.class)
				.getResultList();

		// Von 7 Kunden haben 2 reserviert -> 5 ohne Reservierung
		assertEquals(5, inactiveCustomers.size());
	}

	@Test
	public void testSubqueryWithInOperator() {
		// 5. IN-Subquery: Finde Fahrzeuge, deren Typ zu den leistungsstarken Typen (> 130 PS) gehört
		List<Vehicle> vehicles = manager.createQuery(
				"SELECT v FROM Vehicle v WHERE v.type IN (SELECT t FROM VehicleType t WHERE t.hp > 130)", Vehicle.class)
				.getResultList();

		assertEquals(1, vehicles.size());
		assertEquals("BMW", vehicles.get(0).getType().getModel().getBrand());
	}

	@Test
	public void testSubqueryWithAllOperator() {
		// 6. ALL-Quantor: Finde den Fahrzeugtyp mit der höchsten PS-Zahl (größer-gleich alle anderen Typen)
		List<VehicleType> maxHpTypes = manager.createQuery(
				"SELECT v FROM VehicleType v WHERE v.hp >= ALL (SELECT t.hp FROM VehicleType t)", VehicleType.class)
				.getResultList();

		assertEquals(1, maxHpTypes.size());
		assertEquals("BMW", maxHpTypes.get(0).getModel().getBrand());
		assertEquals(150, maxHpTypes.get(0).getHp());
	}

	@Test
	public void testScalarSubqueryInSelectClause() {
		// 7. Skalar-Subquery im SELECT-Teil: Zähle Reservierungen pro Kunde direkt in der Projektion
		List<Object[]> customerCounts = manager.createQuery(
				"SELECT k.person.lastName, (SELECT COUNT(res) FROM k.reservations res) FROM Customer k WHERE k.person.lastName = 'Mustermann'", Object[].class)
				.getResultList();

		assertEquals(1, customerCounts.size());
		assertEquals("Mustermann", customerCounts.get(0)[0]);
		assertEquals(3L, customerCounts.get(0)[1]);
	}
}
