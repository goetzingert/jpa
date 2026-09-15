package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;

import net.rentacar.model.*;
import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private VehicleType VehicleType;
	private Shop Shop;
	private User user;

	@Override
	public void setUp() throws Exception {
		
		VehicleType = new VehicleType(new Model("VW", "Golf"), 120, 200);
		manager.persist(VehicleType);
		Shop = new Shop("Muenchen");
		manager.persist(Shop);
		user = new User( "Hans", "Mustermann");
		manager.persist(user);
		//manager.persist(user.getPerson());
		manager.flush();
		manager.clear();
	}

	@Test public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class, VehicleType.getId()).getId());
	}

	@Test public void testFindUser() {
		// TODO find User with EntityManager
		assertNotNull(super.manager.find(User.class, user.getId()).getId());
	}

	@Test public void testFindShop() {
		// TODO find Shop with EntityManager
		assertNotNull(super.manager.find(Shop.class, Shop.getId()).getId());
	}

	@Test public void testFindModel() {
		assertNotNull(super.manager.find(VehicleType.class, VehicleType.getId()).getModel()
				.getBrand());
	}
	
	@Test public void testFindPersonByUser() {
		User user = super.manager.find(User.class, this.user.getId());
		Person person = user.getPerson();
		assertNotNull(person
				.getFirstName());
	}

}
