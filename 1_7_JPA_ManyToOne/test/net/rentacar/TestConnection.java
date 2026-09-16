package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.rentacar.model.Model;
import net.rentacar.model.Person;
import net.rentacar.model.Shop;
import net.rentacar.model.User;
import net.rentacar.model.Vehicle;
import net.rentacar.model.VehicleType;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		VehicleType vehicleType = new VehicleType("1", new Model("VW", "Golf"), 120, 200);
		manager.persist(vehicleType);
		Shop shop = new Shop("1", "Muenchen");
		Vehicle vehicle = new Vehicle("1", shop, vehicleType);

		manager.persist(shop);
		manager.persist(new User("1", new Person("1", "Hans", "Mustermann")));
		manager.flush();
		manager.clear();
	}

	@Test
	public void testManyToOneVehicleToType() {
		// 1. Unidirektionale N:1-Navigation von Vehicle zu VehicleType
		Vehicle vehicle = manager.find(Vehicle.class, "1");
		assertNotNull(vehicle);
		assertNotNull(vehicle.getType());
		assertEquals("Golf", vehicle.getType().getModel().getModell());
	}

	@Test
	public void testManyToOneVehicleToLocation() {
		// 2. Bidirektionale N:1-Navigation von Vehicle zu Shop
		Vehicle vehicle = manager.find(Vehicle.class, "1");
		assertNotNull(vehicle);
		assertNotNull(vehicle.getLocation());
		assertEquals("Muenchen", vehicle.getLocation().getLocation());
	}

	@Test
	public void testOneToManyOfShop() {
		// 3. Inverse 1:N-Navigation von Shop zu den zugeordneten Vehicles
		Shop shop = manager.find(Shop.class, "1");
		assertNotNull(shop.getVehicles());
		assertEquals(1, shop.getVehicles().size());
	}

	@Test
	public void testOwningSidePersistsRelation() {
		// 4. Owning Side: Setzen des Standorts am Vehicle schreibt den Fremdschlüssel
		Shop stuttgart = new Shop("2", "Stuttgart");
		manager.persist(stuttgart);

		Vehicle vehicle = manager.find(Vehicle.class, "1");
		
		// TODO: vehicle.setLocation(stuttgart) aufrufen
		vehicle.setLocation(stuttgart);

		manager.flush();
		manager.clear();

		Vehicle reloadedVehicle = manager.find(Vehicle.class, "1");
		assertEquals("Stuttgart", reloadedVehicle.getLocation().getLocation());

		Shop reloadedStuttgart = manager.find(Shop.class, "2");
		assertEquals(1, reloadedStuttgart.getVehicles().size());
	}

	@Test
	public void testDefensiveHelperMethodMaintainsBothSides() {
		// 5. Defensive Synchronisationsmethode shop.addVehicle() pflegt beide Seiten
		Shop shop = manager.find(Shop.class, "1");
		VehicleType type = manager.find(VehicleType.class, "1");

		Vehicle newVehicle = new Vehicle("2", null, type);
		manager.persist(newVehicle);

		// TODO: shop.addVehicle(newVehicle) aufrufen
		shop.addVehicle(newVehicle);

		// Beide Seiten sind im Speicher synchron
		assertEquals(shop, newVehicle.getLocation());
		assertTrue(shop.getVehicles().contains(newVehicle));

		manager.flush();
		manager.clear();

		Vehicle reloaded = manager.find(Vehicle.class, "2");
		assertNotNull(reloaded.getLocation());
		assertEquals("Muenchen", reloaded.getLocation().getLocation());
	}

	@Test
	public void testMovingVehicleUpdatesBothShopCollections() {
		// 6. Umsetzen eines Fahrzeugs aktualisiert die Bestände beider Standorte
		Shop muenchen = manager.find(Shop.class, "1");
		Shop berlin = new Shop("3", "Berlin");
		manager.persist(berlin);

		Vehicle vehicle = manager.find(Vehicle.class, "1");
		vehicle.setLocation(berlin);

		manager.flush();
		manager.clear();

		Shop reloadedMuenchen = manager.find(Shop.class, "1");
		Shop reloadedBerlin = manager.find(Shop.class, "3");

		assertEquals(0, reloadedMuenchen.getVehicles().size(), "München sollte keine Fahrzeuge mehr haben");
		assertEquals(1, reloadedBerlin.getVehicles().size(), "Berlin sollte 1 Fahrzeug haben");
	}

	@Test
	public void testRemovingLocationSetsNullForeignKey() {
		// 7. Lösen des Standorts setzt den Fremdschlüssel auf NULL
		Vehicle vehicle = manager.find(Vehicle.class, "1");
		vehicle.setLocation(null);

		manager.flush();
		manager.clear();

		Vehicle reloaded = manager.find(Vehicle.class, "1");
		assertNull(reloaded.getLocation());
	}

}
