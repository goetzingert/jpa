package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

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
		Set<Vehicle> vehicles = new HashSet<Vehicle>();
		vehicle = new Vehicle(shop, vehicleType);
		vehicles.add(vehicle);
		shop.setVehicles(vehicles);
		manager.persist(vehicle);
		manager.persist(shop);
		user = new User(new Person("Hans", "Mustermann"));
		manager.persist(user);
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindShop() {
		// 1. Laden des Shops
		Shop loadedShop = manager.find(Shop.class, shop.getId());
		assertNotNull(loadedShop);
		assertEquals("Muenchen", loadedShop.getLocation());
	}

	@Test
	public void testOneToManyOfShop() {
		// 2. 1:N-Collection laden und prüfen
		Shop loadedShop = manager.find(Shop.class, shop.getId());
		// TODO: Prüfen, dass shop.getVehicles() nicht null ist und 1 Element enthält
		assertNotNull(loadedShop.getVehicles());
		assertEquals(1, loadedShop.getVehicles().size());
	}

	@Test
	public void testAddingVehicleToShopCollection() {
		// 3. Weiteres Fahrzeug zur Collection hinzufügen
		Shop loadedShop = manager.find(Shop.class, shop.getId());
		VehicleType loadedVehicleType = manager.find(VehicleType.class, vehicleType.getId());

		Vehicle vehicle2 = new Vehicle(loadedShop, loadedVehicleType);
		manager.persist(vehicle2);
		
		// TODO: vehicle2 zu shop.getVehicles() hinzufügen
		loadedShop.getVehicles().add(vehicle2);

		manager.flush();
		manager.clear();

		Shop reloadedShop = manager.find(Shop.class, shop.getId());
		assertEquals(2, reloadedShop.getVehicles().size());
	}

	@Test
	public void testRemovingVehicleFromShopCollectionSetsForeignKeyToNull() {
		// 4. Entfernen aus der 1:N-Collection (ohne orphanRemoval) entkoppelt die Beziehung
		Shop loadedShop = manager.find(Shop.class, shop.getId());
		
		// TODO: shop.getVehicles().clear() aufrufen
		loadedShop.getVehicles().clear();

		manager.flush();
		manager.clear();

		Shop reloadedShop = manager.find(Shop.class, shop.getId());
		assertEquals(0, reloadedShop.getVehicles().size(), "Shop hat keine Fahrzeuge mehr");

		// Das Fahrzeug existiert weiterhin in der DB
		Vehicle loadedVehicle = manager.find(Vehicle.class, vehicle.getId());
		assertNotNull(loadedVehicle, "Fahrzeug existiert weiterhin unabhängig vom Shop");
	}

	@Test
	public void testEmptyShopReturnsEmptyCollectionNotNull() {
		// 5. Neuer Shop ohne Fahrzeuge liefert leere Collection, niemals null
		Shop emptyShop = new Shop("Berlin");
		manager.persist(emptyShop);

		manager.flush();
		manager.clear();

		Shop loaded = manager.find(Shop.class, emptyShop.getId());
		assertNotNull(loaded.getVehicles());
		assertTrue(loaded.getVehicles().isEmpty());
	}

	@Test
	public void testShopLocationUpdatePropagatesOnFlush() {
		// 6. Dirty Checking auf Shop-Attributen
		Shop loadedShop = manager.find(Shop.class, shop.getId());
		loadedShop.setLocation("Muenchen-Zentrum");

		manager.flush();
		manager.clear();

		Shop reloaded = manager.find(Shop.class, shop.getId());
		assertEquals("Muenchen-Zentrum", reloaded.getLocation());
	}

}
