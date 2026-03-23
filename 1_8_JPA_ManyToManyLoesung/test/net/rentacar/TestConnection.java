package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		
		VehicleType vehicleType = new VehicleType("1", new Model("VW", "Golf"), 120, 200);
		manager.persist(vehicleType);
		Shop Shop = new Shop("1", "Muenchen");
		Vehicle vehicle = new Vehicle("1", Shop, vehicleType);
		vehicle.setLocation(new Shop("2", "Stuttgart"));
		manager.persist(vehicle);
		manager.persist(new Nutzer("1", new Person("1", "Hans", "Mustermann")));
		manager.flush();
		manager.clear();
	}

	@Test public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class, "1").getId());
	}

	@Test public void testFindNutzer() {
		// TODO find Nutzer with EntityManager
		assertNotNull(super.manager.find(Nutzer.class, "1").getId());
	}

	@Test public void testFindShop() {
		// TODO find Shop with EntityManager
		assertNotNull(super.manager.find(Shop.class, "1").getId());
	}

	@Test public void testFindModel() {
		assertNotNull(super.manager.find(VehicleType.class, "1").getModel()
				.getBrand());
	}

	@Test public void testFindPersonByNutzer() {
		assertNotNull(super.manager.find(Nutzer.class, "1").getPerson()
				.getFirstName());
	}

	@Test public void testOneToManyOfShop() {
		assertTrue(super.manager.find(Shop.class, "2").getVehicles()
				.toArray().length > 0);
	}

	@Test public void testManyToOneVehicle() {
		assertNotNull(super.manager.find(Vehicle.class, "1").getLocation());
	}

	@Test public void testManyToManyVehicle() {
		assertTrue(super.manager.find(Vehicle.class, "1")
				.getLocationHistory().size() > 0);
	}
}
