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

	private VehicleType vehicleType;
	private Shop muenchen;
	private Shop stuttgart;
	private Vehicle vehicle;
	private User user;

	@Override
	public void setUp() throws Exception {
		vehicleType = new VehicleType(new Model("VW", "Golf"), 120, 200);
		manager.persist(vehicleType);
		muenchen = new Shop("Muenchen");
		stuttgart = new Shop("Stuttgart");
		manager.persist(muenchen);
		manager.persist(stuttgart);
		vehicle = new Vehicle(muenchen, vehicleType);
		vehicle.setLocation(stuttgart);
		manager.persist(vehicle);
		user = new User(new Person("Hans", "Mustermann"));
		manager.persist(user);
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindVehicle() {
		// 1. Fahrzeug laden
		assertNotNull(manager.find(VehicleType.class, vehicleType.getId()).getId());
	}

	@Test
	public void testOneToManyOfShop() {
		// 2. 1:N-Bestand am aktuellen Standort prüfen (Stuttgart hat 1 Fahrzeug)
		Shop shop = manager.find(Shop.class, stuttgart.getId());
		assertNotNull(shop);
		assertTrue(shop.getVehicles().size() > 0);
	}

	@Test
	public void testManyToOneVehicleLocation() {
		// 3. Aktuellen Standort laden
		Vehicle loadedVehicle = manager.find(Vehicle.class, vehicle.getId());
		assertNotNull(loadedVehicle.getLocation());
		assertEquals("Stuttgart", loadedVehicle.getLocation().getLocation());
	}

	@Test
	public void testManyToManyLocationHistory() {
		// 4. N:M-Historie prüfen (München muss in der locationHistory sein)
		Vehicle loadedVehicle = manager.find(Vehicle.class, vehicle.getId());
		// TODO: Prüfen, dass getLocationHistory() nicht null ist, 1 Element enthält und Standort "Muenchen" ist
		assertNotNull(loadedVehicle.getLocationHistory());
		assertEquals(1, loadedVehicle.getLocationHistory().size());
		assertEquals("Muenchen", loadedVehicle.getLocationHistory().get(0).getLocation());
	}

	@Test
	public void testAddingMultipleLocationsToHistory() {
		// 5. Weitere Standortwechsel dokumentieren
		Shop berlin = new Shop("Berlin");
		Shop hamburg = new Shop("Hamburg");
		manager.persist(berlin);
		manager.persist(hamburg);

		Vehicle loadedVehicle = manager.find(Vehicle.class, vehicle.getId());
		
		// TODO: vehicle.setLocation(berlin) und anschließend vehicle.setLocation(hamburg) aufrufen
		loadedVehicle.setLocation(berlin);
		loadedVehicle.setLocation(hamburg);

		manager.flush();
		manager.clear();

		Vehicle reloaded = manager.find(Vehicle.class, vehicle.getId());
		assertEquals("Hamburg", reloaded.getLocation().getLocation());
		// Historie enthält: Muenchen (aus setUp), Stuttgart, Berlin
		assertEquals(3, reloaded.getLocationHistory().size());
	}

	@Test
	public void testMultipleVehiclesSharingSameLocationInHistory() {
		// 6. Echtes N:M-Szenario: Mehrere Fahrzeuge teilen dieselbe historische Station
		Shop koeln = new Shop("Koeln");
		manager.persist(koeln);

		VehicleType type = manager.find(VehicleType.class, vehicleType.getId());
		Vehicle vehicle2 = new Vehicle(koeln, type);
		Shop nuernberg = new Shop("Nuernberg");
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
		assertEquals(vehicle2.getId(), vehiclesWithHistory.get(0).getId());
	}

	@Test
	public void testRemovingHistoryEntryDeletesOnlyJoinTableRow() {
		// 7. Löschen aus der N:M-Collection entfernt nur den Eintrag in der Join-Tabelle, nicht den Shop
		Vehicle loadedVehicle = manager.find(Vehicle.class, vehicle.getId());
		loadedVehicle.getLocationHistory().clear();

		manager.flush();
		manager.clear();

		Vehicle reloaded = manager.find(Vehicle.class, vehicle.getId());
		assertTrue(reloaded.getLocationHistory().isEmpty());

		Shop originalShop = manager.find(Shop.class, muenchen.getId());
		assertNotNull(originalShop, "Shop 'Muenchen' existiert weiterhin in der DB (kein CascadeType.REMOVE)");
	}

}
