package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import net.rentacar.model.Vehicle;
import net.rentacar.model.VehicleType;
import net.rentacar.model.Shop;
import net.rentacar.model.Model;
import net.rentacar.model.Nutzer;
import net.rentacar.model.Person;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		
		VehicleType Vehicle = new VehicleType("1", new Model("VW", "Golf"), 120, 200);
		manager.persist(Vehicle);
		Shop Shop = new Shop("1", "Muenchen");
		Set<Vehicle> Vehicles = new HashSet<Vehicle>();
		Vehicle Vehicle2 = new Vehicle("1",Shop,Vehicle);
		Vehicles.add(Vehicle2);
		Shop.setVehicles(Vehicles );
		manager.persist(Vehicle2);
		manager.persist(Shop);
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
		Shop Shop = super.manager.find(Shop.class, "1");
		manager.clear();
		manager.close();
		assertTrue(Shop.getVehicles()
				.toArray().length > 0);
	}
}
