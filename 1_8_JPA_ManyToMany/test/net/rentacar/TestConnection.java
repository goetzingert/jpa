package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
		vehicle.setLocation(new Shop("2", "Stuttgart"));
		manager.persist(vehicle);
		manager.persist(new User("1", new Person("1", "Hans", "Mustermann")));
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindVehicle() {
		// 1. Fahrzeug laden
		assertNotNull(manager.find(VehicleType.class, "1").getId());
	}

	@Test
	public void testOneToManyOfShop() {
		// 2. 1:N-Bestand am aktuellen Standort prüfen (Stuttgart hat 1 Fahrzeug)
		Shop shop = manager.find(Shop.class, "2");
		assertNotNull(shop);
		assertTrue(shop.getVehicles().size() > 0);
	}

	@Test
	public void testManyToOneVehicleLocation() {
		// 3. Aktuellen Standort laden
		Vehicle vehicle = manager.find(Vehicle.class, "1");
		assertNotNull(vehicle.getLocation());
		assertEquals("Stuttgart", vehicle.getLocation().getLocation());
	}

	@Test
	public void testManyToManyLocationHistory() {
		// 4. N:M-Historie prüfen (München muss in der locationHistory sein)
		Vehicle vehicle = manager.find(Vehicle.class, "1");
		// TODO: Prüfen, dass getLocationHistory() nicht null ist, 1 Element enthält und Standort "Muenchen" ist
		assertNotNull(vehicle.getLocationHistory());
		assertEquals(1, vehicle.getLocationHistory().size());
		assertEquals("Muenchen", vehicle.getLocationHistory().get(0).getLocation());
	}

	@Test
	public void testAddingMultipleLocationsToHistory() {
		// 5. Weitere Standortwechsel dokumentieren
		Shop berlin = new Shop("3", "Berlin");
		Shop hamburg = new Shop("4", "Hamburg");
		manager.persist(berlin);
		manager.persist(hamburg);

		Vehicle vehicle = manager.find(Vehicle.class, "1");
		
		// TODO: vehicle.setLocation(berlin) und anschließend vehicle.setLocation(hamburg) aufrufen
		vehicle.setLocation(berlin);
		vehicle.setLocation(hamburg);

		manager.flush();
		manager.clear();

		Vehicle reloaded = manager.find(Vehicle.class, "1");
		assertEquals("Hamburg", reloaded.getLocation().getLocation());
		// Historie enthält: Muenchen (aus setUp), Stuttgart, Berlin
		assertEquals(3, reloaded.getLocationHistory().size());
	}

	@Test
	public void testMultipleVehiclesSharingSameLocationInHistory() {
		// 6. Echtes N:M-Szenario: Mehrere Fahrzeuge teilen dieselbe historische Station
		Shop koeln = new Shop("5", "Koeln");
		manager.persist(koeln);

		VehicleType type = manager.find(VehicleType.class, "1");
		Vehicle vehicle2 = new Vehicle("2", koeln, type);
		Shop nuernberg = new Shop("6", "Nuernberg");
		manager.persist(nuernberg);
		vehicle2.setLocation(nuernberg);
		manager.persist(vehicle2);

		manager.flush();
		manager.clear();

		// JPQL-Query über die N:M-Verbindungstabelle
		List<Vehicle> vehiclesWithHistory = manager.createQuery(
				"SELECT v FROM Vehicle v JOIN v.locationHistory h WHERE h.location = :loc", Vehicle.class)
				.setParameter("loc", "Koeln")
				.getResultList();

		assertEquals(1, vehiclesWithHistory.size());
		assertEquals("2", vehiclesWithHistory.get(0).getId());
	}

	@Test
	public void testRemovingHistoryEntryDeletesOnlyJoinTableRow() {
		// 7. Löschen aus der N:M-Collection entfernt nur den Eintrag in der Join-Tabelle, nicht den Shop
		Vehicle vehicle = manager.find(Vehicle.class, "1");
		vehicle.getLocationHistory().clear();

		manager.flush();
		manager.clear();

		Vehicle reloaded = manager.find(Vehicle.class, "1");
		assertTrue(reloaded.getLocationHistory().isEmpty());

		Shop originalShop = manager.find(Shop.class, "1");
		assertNotNull(originalShop, "Shop 'Muenchen' existiert weiterhin in der DB (kein CascadeType.REMOVE)");
	}

}
