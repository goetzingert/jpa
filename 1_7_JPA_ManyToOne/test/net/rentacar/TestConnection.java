package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import net.rentacar.model.VehicleType;
import net.rentacar.model.Vehicle;
import net.rentacar.model.Shop;
import net.rentacar.model.Model;
import net.rentacar.model.User;
import net.rentacar.model.Person;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private VehicleType Vehicle;
	private Vehicle Vehicle2;
	private User user;
	private Shop Shop;

	@Override
	public void setUp() throws Exception {
		
		Vehicle = new VehicleType( new Model("VW", "Golf"), 120, 200);
		manager.persist(Vehicle);
		Shop = new Shop( "Muenchen");
		Set<Vehicle> Vehicles = new HashSet<Vehicle>();
		Vehicle2 = new Vehicle(Shop, Vehicle);
		Vehicles.add(Vehicle2);
		Shop.setVehicles(Vehicles);
		manager.persist(Shop);
		user = new User( new Person("Hans", "Mustermann"));
		manager.persist(user);
		manager.flush();
		manager.clear();

	}

	@Test public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class, Vehicle.getId()).getId());
	}

	@Test public void testFindUser() {
		// TODO find User with EntityManager
		assertNotNull(super.manager.find(User.class, user.getId() ).getId());
	}

	@Test public void testFindShop() {
		// TODO find Shop with EntityManager
		assertNotNull(super.manager.find(Shop.class, Shop.getId()).getId());
	}

	@Test public void testFindModel() {
		assertNotNull(super.manager.find(VehicleType.class, Vehicle.getId()).getModel()
				.getBrand());
	}

	@Test public void testFindPersonByUser() {
		assertNotNull(super.manager.find(User.class,user.getId()).getPerson()
				.getFirstName());
	}

	@Test public void testOneToManyOfShop() {
		assertTrue(super.manager.find(Shop.class, Shop.getId()).getVehicles()
				.toArray().length > 0);
	}

	@Test public void testManyToOneVehicle() {
		assertNotNull(super.manager.find(Vehicle.class, Vehicle2.getId()).getLocation());
	}
}
