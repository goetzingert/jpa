package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.LockModeType;

import net.rentacar.model.VehicleType;
import net.rentacar.model.VehicleItem;
import net.rentacar.model.Shop;
import net.rentacar.model.Truck;
import net.rentacar.model.Model;
import net.rentacar.model.Nutzer;
import net.rentacar.model.Person;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		
		VehicleType Vehicle = new Truck("1", new Model("VW", "Golf"), 120, 200,10000);
		manager.persist(Vehicle);
		Shop Shop = new Shop("1", "Muenchen");
		VehicleItem VehicleItem = new VehicleItem("1", Shop, Vehicle);
		VehicleItem.setLocation(new Shop("2", "Stuttgart"));
		manager.persist(VehicleItem);
		manager.persist(new Nutzer("1", new Person("1", "Hans", "Mustermann")));
		manager.lock(Shop, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
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

	@Test public void testManyToOneVehicleItem() {
		assertNotNull(super.manager.find(VehicleItem.class, "1").getLocation());
	}

	@Test public void testManyToManyVehicleItem() {
		assertTrue(super.manager.find(VehicleItem.class, "1")
				.getLocationHistory().size() > 0);
	}
	
	@Test public void testInheritance() {
		assertNotNull(super.manager.find(Truck.class, "1"));
	}
}
