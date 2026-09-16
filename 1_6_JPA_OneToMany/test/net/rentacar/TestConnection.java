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

	@Override
	public void setUp() throws Exception {
		VehicleType vehicleType = new VehicleType("1", new Model("VW", "Golf"), 120, 200);
		manager.persist(vehicleType);
		Shop shop = new Shop("1", "Muenchen");
		Set<Vehicle> vehicles = new HashSet<Vehicle>();
		Vehicle vehicle = new Vehicle("1", shop, vehicleType);
		vehicles.add(vehicle);
		shop.setVehicles(vehicles);
		manager.persist(vehicle);
		manager.persist(shop);
		manager.persist(new User("1", new Person("1", "Hans", "Mustermann")));
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindShop() {
		// 1. Laden des Shops
		Shop shop = manager.find(Shop.class, "1");
		assertNotNull(shop);
		assertEquals("Muenchen", shop.getLocation());
	}

	@Test
	public void testOneToManyOfShop() {
		// 2. 1:N-Collection laden und prüfen
		Shop shop = manager.find(Shop.class, "1");
		// TODO: Prüfen, dass shop.getVehicles() nicht null ist und 1 Element enthält
		assertNotNull(shop.getVehicles());
		assertEquals(1, shop.getVehicles().size());
	}

	@Test
	public void testAddingVehicleToShopCollection() {
		// 3. Weiteres Fahrzeug zur Collection hinzufügen
		Shop shop = manager.find(Shop.class, "1");
		VehicleType vehicleType = manager.find(VehicleType.class, "1");

		Vehicle vehicle2 = new Vehicle("2", shop, vehicleType);
		manager.persist(vehicle2);
		
		// TODO: vehicle2 zu shop.getVehicles() hinzufügen
		shop.getVehicles().add(vehicle2);

		manager.flush();
		manager.clear();

		Shop reloadedShop = manager.find(Shop.class, "1");
		assertEquals(2, reloadedShop.getVehicles().size());
	}

	@Test
	public void testRemovingVehicleFromShopCollectionSetsForeignKeyToNull() {
		// 4. Entfernen aus der 1:N-Collection (ohne orphanRemoval) entkoppelt die Beziehung
		Shop shop = manager.find(Shop.class, "1");
		
		// TODO: shop.getVehicles().clear() aufrufen
		shop.getVehicles().clear();

		manager.flush();
		manager.clear();

		Shop reloadedShop = manager.find(Shop.class, "1");
		assertEquals(0, reloadedShop.getVehicles().size(), "Shop hat keine Fahrzeuge mehr");

		// Das Fahrzeug existiert weiterhin in der DB
		Vehicle vehicle = manager.find(Vehicle.class, "1");
		assertNotNull(vehicle, "Fahrzeug existiert weiterhin unabhängig vom Shop");
	}

	@Test
	public void testEmptyShopReturnsEmptyCollectionNotNull() {
		// 5. Neuer Shop ohne Fahrzeuge liefert leere Collection, niemals null
		Shop emptyShop = new Shop("2", "Berlin");
		manager.persist(emptyShop);

		manager.flush();
		manager.clear();

		Shop loaded = manager.find(Shop.class, "2");
		assertNotNull(loaded.getVehicles());
		assertTrue(loaded.getVehicles().isEmpty());
	}

	@Test
	public void testShopLocationUpdatePropagatesOnFlush() {
		// 6. Dirty Checking auf Shop-Attributen
		Shop shop = manager.find(Shop.class, "1");
		shop.setLocation("Muenchen-Zentrum");

		manager.flush();
		manager.clear();

		Shop reloaded = manager.find(Shop.class, "1");
		assertEquals("Muenchen-Zentrum", reloaded.getLocation());
	}

}
