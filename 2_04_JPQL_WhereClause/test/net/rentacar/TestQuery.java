package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;

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
		// 1. IN-Operator mit einer List-Bind-Variable :FIRSTNAME
		List<String> list = Arrays.asList("Michael", "Mathias");
		List<User> users = null; // TODO: "SELECT n FROM User n WHERE n.person.firstName IN :FIRSTNAME ORDER BY n.person.firstName"

		assertNotNull(users);
		assertEquals(2, users.size());
		assertEquals("Mathias", users.get(0).getPerson().getFirstName());
		assertEquals("Michael", users.get(1).getPerson().getFirstName());
	}

	@Test
	public void testQueryForShopMitKeinemVehicle() {
		// 2. IS EMPTY Operator auf Assoziations-Collections (carpool)
		List<Shop> emptyShops = null; // TODO: "SELECT f FROM Shop f WHERE f.carpool IS EMPTY ORDER BY f.location"

		assertNotNull(emptyShops);
		assertEquals(2, emptyShops.size());
		assertEquals("Koeln", emptyShops.get(0).getLocation());
		assertEquals("Muenchen", emptyShops.get(1).getLocation());
	}

	@Test
	public void testQueryForShopsNichtInStuttgartUndMuenchen_MitNOTINOperator() {
		// 3. NOT IN Operator
		List<String> orte = Arrays.asList("Muenchen", "Stuttgart");
		List<Shop> shops = null; // TODO: "SELECT f FROM Shop f WHERE f.location NOT IN :orte"

		assertNotNull(shops);
		assertEquals(1, shops.size());
		assertEquals("Koeln", shops.get(0).getLocation());
	}

	@Test
	public void testQueryBetweenOperatorInclusiveBoundaries() {
		// 4. BETWEEN-Operator: Prüfe PS-Zahlen zwischen 120 und 140 (inklusive Grenzen)
		List<VehicleType> result = null; // TODO: "SELECT f FROM VehicleType f WHERE f.hp BETWEEN :minHp AND :maxHp ORDER BY f.model.brand"

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("Mercedes", result.get(0).getModel().getBrand());
		assertEquals("VW", result.get(1).getModel().getBrand());
	}

	@Test
	public void testQueryMemberOfCollectionOperator() {
		// 5. MEMBER OF Operator: Prüfe, welcher Shop das gegebene Fahrzeug im Fuhrpark (carpool) hält
		Vehicle persistedVehicle = manager.find(Vehicle.class, vehicle.getId());
		assertNotNull(persistedVehicle);

		Shop stationWithVehicle = null; // TODO: "SELECT s FROM Shop s WHERE :veh MEMBER OF s.carpool"

		assertNotNull(stationWithVehicle);
		assertEquals("Stuttgart", stationWithVehicle.getLocation());
	}

	@Test
	public void testQueryStringLengthFunctionInWhereClause() {
		// 6. Skalarfunktionen in WHERE: LENGTH() > 7
		List<User> longNamedUsers = null; // TODO: "SELECT u FROM User u WHERE LENGTH(u.person.lastName) > 7 ORDER BY u.person.lastName"

		assertNotNull(longNamedUsers);
		assertEquals(2, longNamedUsers.size());
		assertEquals("Anstaedt", longNamedUsers.get(0).getPerson().getLastName());
		assertEquals("Mustermann", longNamedUsers.get(1).getPerson().getLastName());
	}

	@Test
	public void testQueryIsNullAndIsNotNullOperators() {
		// 7. IS NULL und IS NOT NULL
		List<Vehicle> withLocation = null; // TODO: "SELECT v FROM Vehicle v WHERE v.location IS NOT NULL"
		assertNotNull(withLocation);
		assertEquals(1, withLocation.size());

		List<Vehicle> withoutLocation = null; // TODO: "SELECT v FROM Vehicle v WHERE v.location IS NULL"
		assertNotNull(withoutLocation);
		assertTrue(withoutLocation.isEmpty());
	}
}
