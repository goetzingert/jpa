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
		// 1. Verwende einen benannten Parameter :hp in der WHERE-Klausel
		List<VehicleType> result = null; // TODO: Query mit .setParameter("hp", 130)

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("BMW", result.get(0).getModel().getBrand());
	}

	@Test
	public void testQueryWithPositionalParameter() {
		// 2. Verwende einen 1-basierten Positions-Parameter ?1
		List<VehicleType> result = null; // TODO: Query mit .setParameter(1, 130)

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("BMW", result.get(0).getModel().getBrand());
	}

	@Test
	public void testQueryWithMultipleNamedParametersAndLogicalAnd() {
		// 3. Verknüpfe zwei benannte Parameter (:minHp und :brand) mit AND
		List<VehicleType> result = null; // TODO: WHERE f.hp >= :minHp AND f.model.brand = :brand

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Golf", result.get(0).getModel().getModell());
	}

	@Test
	public void testQueryWithCaseInsensitiveLikePattern() {
		// 4. Case-insensitives Matching mit LOWER() und Wildcard-Parameter :pattern
		List<VehicleType> result = null; // TODO: WHERE LOWER(f.model.modell) LIKE LOWER(:pattern) mit "%ol%"

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Golf", result.get(0).getModel().getModell());
	}

	@Test
	public void testQueryWithEntityAsParameter() {
		// 5. Übergabe einer Entity-Instanz als Parameter :type
		// JPA vergleicht automatisch über den Fremdschlüssel
		VehicleType persistedType = manager.find(VehicleType.class, carVw.getId());
		assertNotNull(persistedType);

		List<Vehicle> vehicles = null; // TODO: WHERE v.type = :type

		assertNotNull(vehicles);
		assertEquals(1, vehicles.size());
		assertEquals("Stuttgart", vehicles.get(0).getLocation().getLocation());
	}

	@Test
	public void testQueryNullParameterDemonstratesIsNullRequirement() {
		// 6. Randfall: Warum liefert "WHERE f.model.brand = :brand" mit :brand = null 0 Treffer?
		// In SQL ist NULL = NULL unbekannt (FALSE). Für NULL-Prüfungen muss IS NULL / IS NOT NULL genutzt werden.
		List<VehicleType> equalNullResult = null; // TODO: Query mit f.model.brand = :brand und setParameter("brand", null)
		assertNotNull(equalNullResult);
		assertEquals(0, equalNullResult.size());

		List<VehicleType> isNullResult = null; // TODO: Query mit f.model.brand IS NOT NULL
		assertNotNull(isNullResult);
		assertEquals(3, isNullResult.size());
	}
}
