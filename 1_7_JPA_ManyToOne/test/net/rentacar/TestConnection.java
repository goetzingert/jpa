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

	private VehicleType vehicleType;
	private Shop shop;
	private Vehicle vehicle;
	private User user;

	@Override
	public void setUp() throws Exception {
		vehicleType = new VehicleType(new Model("VW", "Golf"), 120, 200);
		manager.persist(vehicleType);
		shop = new Shop("Muenchen");
		vehicle = new Vehicle(shop, vehicleType);
		shop.addVehicle(vehicle);
		manager.persist(vehicle);
		manager.persist(shop);
		user = new User(new Person("Hans", "Mustermann"));
		manager.persist(user);
		manager.flush();
		manager.clear();
	}

	@Test
	public void testManyToOneVehicleToType() {
		// 1. Unidirektionale N:1-Navigation von Vehicle zu VehicleType
		Vehicle loadedVehicle = manager.find(Vehicle.class, vehicle.getId());
		assertNotNull(loadedVehicle);
		assertNotNull(loadedVehicle.getType());
		assertEquals("Golf", loadedVehicle.getType().getModel().getModell());
	}

	@Test
	public void testManyToOneVehicleToLocation() {
		// 2. Bidirektionale N:1-Navigation von Vehicle zu Shop
		Vehicle loadedVehicle = manager.find(Vehicle.class, vehicle.getId());
		assertNotNull(loadedVehicle);
		assertNotNull(loadedVehicle.getLocation());
		assertEquals("Muenchen", loadedVehicle.getLocation().getLocation());
	}

	@Test
	public void testOneToManyOfShop() {
		// 3. Inverse 1:N-Navigation von Shop zu den zugeordneten Vehicles
		Shop loadedShop = manager.find(Shop.class, shop.getId());
		assertNotNull(loadedShop.getVehicles());
		assertEquals(1, loadedShop.getVehicles().size());
	}

	@Test
	public void testOwningSidePersistsRelation() {
		// 4. Owning Side: Setzen des Standorts am Vehicle schreibt den Fremdschlüssel
		Shop stuttgart = new Shop("Stuttgart");
		manager.persist(stuttgart);

		Vehicle loadedVehicle = manager.find(Vehicle.class, vehicle.getId());
		
		// TODO: vehicle.setLocation(stuttgart) aufrufen
		loadedVehicle.setLocation(stuttgart);

		manager.flush();
		manager.clear();

		Vehicle reloadedVehicle = manager.find(Vehicle.class, vehicle.getId());
		assertEquals("Stuttgart", reloadedVehicle.getLocation().getLocation());

		Shop reloadedStuttgart = manager.find(Shop.class, stuttgart.getId());
		assertEquals(1, reloadedStuttgart.getVehicles().size());
	}

	@Test
	public void testDefensiveHelperMethodMaintainsBothSides() {
		// 5. Defensive Synchronisationsmethode shop.addVehicle() pflegt beide Seiten
		Shop loadedShop = manager.find(Shop.class, shop.getId());
		VehicleType type = manager.find(VehicleType.class, vehicleType.getId());

		Vehicle newVehicle = new Vehicle(null, type);
		manager.persist(newVehicle);

		// TODO: shop.addVehicle(newVehicle) aufrufen
		loadedShop.addVehicle(newVehicle);

		// Beide Seiten sind im Speicher synchron
		assertEquals(loadedShop, newVehicle.getLocation());
		assertTrue(loadedShop.getVehicles().contains(newVehicle));

		manager.flush();
		manager.clear();

		Vehicle reloaded = manager.find(Vehicle.class, newVehicle.getId());
		assertNotNull(reloaded.getLocation());
		assertEquals("Muenchen", reloaded.getLocation().getLocation());
	}

	@Test
	public void testMovingVehicleUpdatesBothShopCollections() {
		// 6. Umsetzen eines Fahrzeugs aktualisiert die Bestände beider Standorte
		Shop muenchen = manager.find(Shop.class, shop.getId());
		Shop berlin = new Shop("Berlin");
		manager.persist(berlin);

		Vehicle loadedVehicle = manager.find(Vehicle.class, vehicle.getId());
		loadedVehicle.setLocation(berlin);

		manager.flush();
		manager.clear();

		Shop reloadedMuenchen = manager.find(Shop.class, shop.getId());
		Shop reloadedBerlin = manager.find(Shop.class, berlin.getId());

		assertEquals(0, reloadedMuenchen.getVehicles().size(), "München sollte keine Fahrzeuge mehr haben");
		assertEquals(1, reloadedBerlin.getVehicles().size(), "Berlin sollte 1 Fahrzeug haben");
	}

	@Test
	public void testRemovingLocationSetsNullForeignKey() {
		// 7. Lösen des Standorts setzt den Fremdschlüssel auf NULL
		Vehicle loadedVehicle = manager.find(Vehicle.class, vehicle.getId());
		loadedVehicle.setLocation(null);

		manager.flush();
		manager.clear();

		Vehicle reloaded = manager.find(Vehicle.class, vehicle.getId());
		assertNull(reloaded.getLocation());
	}

}
