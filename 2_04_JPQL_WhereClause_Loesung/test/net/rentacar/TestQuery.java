package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	private Vehicle vehicle;

	@Override
	public void setUp() throws Exception {

		VehicleType vehicleType1 = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(vehicleType1);
		VehicleType vehicleType2 = new Truck(new Model("Mercedes", "10to"), 120, 200, 10000);
		manager.persist(vehicleType2);
		VehicleType vehicleType3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(vehicleType3);

		Shop muenchen = new Shop("Muenchen");
		vehicle = new Vehicle(muenchen, vehicleType1);
		Shop stuttgart = new Shop("Stuttgart");
		vehicle.setLocation(stuttgart);
		manager.persist(vehicle);

		manager.persist(new User(new Person("Hans", "Mustermann")));
		manager.persist(new User(new Person("Franz", "Mueller")));
		manager.persist(new User(new Person("Herbert", "Schmitt")));
		manager.persist(new User(new Person("Ingo", "Meyer")));
		manager.persist(new User(new Person("Mathias", "Mayer")));
		manager.persist(new User(new Person("Michael", "Anstaedt")));
		manager.persist(new User(new Person("Ralf", "Gross")));

		Shop koeln = new Shop("Koeln");
		manager.persist(koeln);
		
		manager.flush();
		manager.clear();
	}

	@Test
	public void testQueryForUserWithPersonnameISMichaelOrMathias() {
		// 1. IN-Operator mit List-Parameter
		List<String> list = Arrays.asList("Michael", "Mathias");
		List<User> users = manager.createQuery(
				"SELECT n FROM User n WHERE n.person.firstName IN :FIRSTNAME ORDER BY n.person.firstName", User.class)
				.setParameter("FIRSTNAME", list)
				.getResultList();

		assertEquals(2, users.size());
		assertEquals("Mathias", users.get(0).getPerson().getFirstName());
		assertEquals("Michael", users.get(1).getPerson().getFirstName());
	}

	@Test
	public void testQueryForShopMitKeinemVehicle() {
		// 2. IS EMPTY Operator auf Collection-Beziehungen (@OneToMany carpool)
		// Muenchen und Koeln haben keine zugewiesenen Fahrzeuge
		List<Shop> emptyShops = manager.createQuery(
				"SELECT f FROM Shop f WHERE f.carpool IS EMPTY ORDER BY f.location", Shop.class)
				.getResultList();

		assertEquals(2, emptyShops.size());
		assertEquals("Koeln", emptyShops.get(0).getLocation());
		assertEquals("Muenchen", emptyShops.get(1).getLocation());
	}

	@Test
	public void testQueryForShopsNichtInStuttgartUndMuenchen_MitNOTINOperator() {
		// 3. NOT IN Operator
		List<String> orte = Arrays.asList("Muenchen", "Stuttgart");
		List<Shop> shops = manager.createQuery(
				"SELECT f FROM Shop f WHERE f.location NOT IN :orte", Shop.class)
				.setParameter("orte", orte)
				.getResultList();

		assertEquals(1, shops.size());
		assertEquals("Koeln", shops.get(0).getLocation());
	}

	@Test
	public void testQueryBetweenOperatorInclusiveBoundaries() {
		// 4. BETWEEN-Operator: Beide Grenzwerte sind inklusiv (BETWEEN 120 AND 140 schließt 120 ein, 150 aus)
		List<VehicleType> result = manager.createQuery(
				"SELECT f FROM VehicleType f WHERE f.hp BETWEEN :minHp AND :maxHp ORDER BY f.model.brand", VehicleType.class)
				.setParameter("minHp", 120)
				.setParameter("maxHp", 140)
				.getResultList();

		assertEquals(2, result.size());
		assertEquals("Mercedes", result.get(0).getModel().getBrand());
		assertEquals("VW", result.get(1).getModel().getBrand());
	}

	@Test
	public void testQueryMemberOfCollectionOperator() {
		// 5. MEMBER OF Operator: Prüft, ob ein Entity-Element in einer Collection-Assoziation enthalten ist
		Vehicle persistedVehicle = manager.find(Vehicle.class, vehicle.getId());
		assertNotNull(persistedVehicle);

		Shop stationWithVehicle = manager.createQuery(
				"SELECT s FROM Shop s WHERE :veh MEMBER OF s.carpool", Shop.class)
				.setParameter("veh", persistedVehicle)
				.getSingleResult();

		assertEquals("Stuttgart", stationWithVehicle.getLocation());
	}

	@Test
	public void testQueryStringLengthFunctionInWhereClause() {
		// 6. JPQL-Skalarfunktionen in WHERE: LENGTH() und UPPER()
		// Nachnamen mit mehr als 7 Zeichen: Mustermann (10), Anstaedt (8)
		List<User> longNamedUsers = manager.createQuery(
				"SELECT u FROM User u WHERE LENGTH(u.person.lastName) > 7 ORDER BY u.person.lastName", User.class)
				.getResultList();

		assertEquals(2, longNamedUsers.size());
		assertEquals("Anstaedt", longNamedUsers.get(0).getPerson().getLastName());
		assertEquals("Mustermann", longNamedUsers.get(1).getPerson().getLastName());
	}

	@Test
	public void testQueryIsNullAndIsNotNullOperators() {
		// 7. IS NULL und IS NOT NULL Operatoren
		List<Vehicle> withLocation = manager.createQuery(
				"SELECT v FROM Vehicle v WHERE v.location IS NOT NULL", Vehicle.class)
				.getResultList();

		assertEquals(1, withLocation.size());

		List<Vehicle> withoutLocation = manager.createQuery(
				"SELECT v FROM Vehicle v WHERE v.location IS NULL", Vehicle.class)
				.getResultList();

		assertTrue(withoutLocation.isEmpty());
	}
}
