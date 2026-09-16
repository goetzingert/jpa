package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.GregorianCalendar;
import java.util.List;

import net.rentacar.dto.ReservationStatsDTO;
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
	public void testGroupBy() {
		// 1. GROUP BY mit COUNT: Zähle Reservierungen pro Fahrzeugmodell
		List<Object[]> resultList = null; // TODO: "SELECT res.vehicle.type.model.modell, COUNT(res) FROM Customer k, IN (k.reservations) res GROUP BY res.vehicle.type.model.modell ORDER BY res.vehicle.type.model.modell"

		assertNotNull(resultList);
		assertEquals(2, resultList.size());
		assertEquals("323", resultList.get(0)[0]);
		assertEquals(3L, resultList.get(0)[1]);
		assertEquals("Golf", resultList.get(1)[0]);
		assertEquals(2L, resultList.get(1)[1]);
	}

	@Test
	public void testGroupByAndHaving() {
		// 2. HAVING-Klausel: Filtere aggregierte Gruppen nach Anzahl > 2
		List<Object[]> resultList = null; // TODO: "SELECT res.vehicle.type.model.modell, COUNT(res) FROM Customer k, IN (k.reservations) res GROUP BY res.vehicle.type.model.modell HAVING COUNT(res) > 2"

		assertNotNull(resultList);
		assertEquals(1, resultList.size());
		assertEquals("323", resultList.get(0)[0]);
		assertEquals(3L, resultList.get(0)[1]);
	}

	@Test
	public void testMultipleAggregatesSumMinMaxAvg() {
		// 3. Mehrere Aggregatfunktionen gleichzeitig: COUNT, SUM, AVG, MIN, MAX
		List<Object[]> resultList = null; // TODO: "SELECT res.vehicle.type.model.modell, COUNT(res), SUM(res.price), AVG(res.price), MIN(res.price), MAX(res.price) FROM Customer k, IN (k.reservations) res GROUP BY res.vehicle.type.model.modell ORDER BY res.vehicle.type.model.modell"

		assertNotNull(resultList);
		assertEquals(2, resultList.size());

		// Modell "323": 3 Reservierungen (Preise: 220, 330, 440 -> Summe 990, Schnitt 330, Min 220, Max 440)
		Object[] rowBmw = resultList.get(0);
		assertEquals("323", rowBmw[0]);
		assertEquals(3L, rowBmw[1]);
		assertEquals(990.0, ((Number) rowBmw[2]).doubleValue(), 0.01);
		assertEquals(330.0, ((Number) rowBmw[3]).doubleValue(), 0.01);
		assertEquals(220.0f, ((Number) rowBmw[4]).floatValue(), 0.01);
		assertEquals(440.0f, ((Number) rowBmw[5]).floatValue(), 0.01);

		// Modell "Golf": 2 Reservierungen (Preise: 100, 110 -> Summe 210, Schnitt 105, Min 100, Max 110)
		Object[] rowVw = resultList.get(1);
		assertEquals("Golf", rowVw[0]);
		assertEquals(2L, rowVw[1]);
		assertEquals(210.0, ((Number) rowVw[2]).doubleValue(), 0.01);
		assertEquals(105.0, ((Number) rowVw[3]).doubleValue(), 0.01);
	}

	@Test
	public void testCountDistinctModels() {
		// 4. COUNT(DISTINCT ...): Ermittle die Anzahl eindeutiger reservierter Fahrzeugmodelle
		Long distinctModels = null; // TODO: "SELECT COUNT(DISTINCT res.vehicle.type.model.modell) FROM Customer k, IN (k.reservations) res"

		assertNotNull(distinctModels);
		assertEquals(2L, distinctModels);
	}

	@Test
	public void testGroupByWithDtoProjection() {
		// 5. Constructor Expression kombiniert mit GROUP BY: Aggregierte Kennzahlen typsicher in DTOs mappen
		List<ReservationStatsDTO> stats = null; // TODO: "SELECT new " + ReservationStatsDTO.class.getName() + "(res.vehicle.type.model.modell, COUNT(res), SUM(res.price)) FROM Customer k, IN (k.reservations) res GROUP BY res.vehicle.type.model.modell ORDER BY res.vehicle.type.model.modell"

		assertNotNull(stats);
		assertEquals(2, stats.size());
		assertEquals("323", stats.get(0).getModel());
		assertEquals(3L, stats.get(0).getCount());
		assertEquals(990.0, stats.get(0).getTotalPrice(), 0.01);

		assertEquals("Golf", stats.get(1).getModel());
		assertEquals(2L, stats.get(1).getCount());
		assertEquals(210.0, stats.get(1).getTotalPrice(), 0.01);
	}

	@Test
	public void testHavingWithSumPriceCondition() {
		// 6. HAVING auf Umsatz: Finde Kunden, deren Gesamtumsatz über 500 liegt
		List<Object[]> highSpenders = null; // TODO: "SELECT k.person.lastName, SUM(res.price) FROM Customer k, IN (k.reservations) res GROUP BY k.person.lastName HAVING SUM(res.price) > 500"

		assertNotNull(highSpenders);
		assertEquals(1, highSpenders.size());
		assertEquals("Mustermann", highSpenders.get(0)[0]);
		assertEquals(760.0, ((Number) highSpenders.get(0)[1]).doubleValue(), 0.01);
	}
}
