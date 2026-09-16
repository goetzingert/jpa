package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	private VehicleType carVw;
	private VehicleType truckMercedes;
	private VehicleType carBmw;
	private Vehicle vehicle;

	@Override
	public void setUp() throws Exception {
		
		carVw = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(carVw);
		truckMercedes = new Truck(new Model("Mercedes", "10to"), 120, 200, 10000);
		manager.persist(truckMercedes);
		carBmw = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(carBmw);
		Shop shop = new Shop("Muenchen");
		vehicle = new Vehicle(shop, carVw);
		vehicle.setLocation(new Shop("Stuttgart"));
		manager.persist(vehicle);
		manager.persist(new User(new Person("Hans", "Mustermann")));
		manager.flush();
		manager.clear();
	}

	@Test
	public void testQueryForVehicleTypesMoreThan130HPWithNamedParameter() {
		// 1. Benannter Parameter :hp
		List<VehicleType> result = manager.createQuery(
				"SELECT f FROM VehicleType f WHERE f.hp > :hp", VehicleType.class)
				.setParameter("hp", 130)
				.getResultList();

		assertEquals(1, result.size());
		assertEquals("BMW", result.get(0).getModel().getBrand());
	}

	@Test
	public void testQueryWithPositionalParameter() {
		// 2. Positionsbezogener Parameter ?1 (JPA-Standard: 1-basiert)
		List<VehicleType> result = manager.createQuery(
				"SELECT f FROM VehicleType f WHERE f.hp > ?1", VehicleType.class)
				.setParameter(1, 130)
				.getResultList();

		assertEquals(1, result.size());
		assertEquals("BMW", result.get(0).getModel().getBrand());
	}

	@Test
	public void testQueryWithMultipleNamedParametersAndLogicalAnd() {
		// 3. Mehrere Parameter (:minHp und :brand) kombiniert mit AND
		List<VehicleType> result = manager.createQuery(
				"SELECT f FROM VehicleType f WHERE f.hp >= :minHp AND f.model.brand = :brand", VehicleType.class)
				.setParameter("minHp", 120)
				.setParameter("brand", "VW")
				.getResultList();

		assertEquals(1, result.size());
		assertEquals("Golf", result.get(0).getModel().getModell());
	}

	@Test
	public void testQueryWithCaseInsensitiveLikePattern() {
		// 4. Case-insensitives LIKE mit LOWER() und Wildcard-Parameter :pattern
		List<VehicleType> result = manager.createQuery(
				"SELECT f FROM VehicleType f WHERE LOWER(f.model.modell) LIKE LOWER(:pattern)", VehicleType.class)
				.setParameter("pattern", "%ol%")
				.getResultList();

		assertEquals(1, result.size());
		assertEquals("Golf", result.get(0).getModel().getModell());
	}

	@Test
	public void testQueryWithEntityAsParameter() {
		// 5. Übergabe einer kompletten Entity-Instanz als Parameter :type
		// JPA vergleicht hierbei automatisch den Primärschlüssel (Foreign Key in SQL)
		VehicleType persistedType = manager.find(VehicleType.class, carVw.getId());
		assertNotNull(persistedType);

		List<Vehicle> vehicles = manager.createQuery(
				"SELECT v FROM Vehicle v WHERE v.type = :type", Vehicle.class)
				.setParameter("type", persistedType)
				.getResultList();

		assertEquals(1, vehicles.size());
		assertEquals("Stuttgart", vehicles.get(0).getLocation().getLocation());
	}

	@Test
	public void testQueryNullParameterDemonstratesIsNullRequirement() {
		// 6. Randfall: Warum liefert "WHERE f.model.brand = :brand" mit :brand = null 0 Treffer?
		// In SQL ist NULL = NULL unbekannt (FALSE). Für NULL-Prüfungen muss IS NULL / IS NOT NULL genutzt werden.
		List<VehicleType> equalNullResult = manager.createQuery(
				"SELECT f FROM VehicleType f WHERE f.model.brand = :brand", VehicleType.class)
				.setParameter("brand", null)
				.getResultList();

		assertEquals(0, equalNullResult.size()); // Kein Treffer wegen 3-wertiger Logik

		List<VehicleType> isNullResult = manager.createQuery(
				"SELECT f FROM VehicleType f WHERE f.model.brand IS NOT NULL", VehicleType.class)
				.getResultList();

		assertEquals(3, isNullResult.size());
	}
}
