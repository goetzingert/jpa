package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.VehicleType;
import net.rentacar.model.Shop;
import net.rentacar.model.User;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		
		manager.persist(new VehicleType("1", "VW", "Golf", 120, 200));
		manager.persist(new Shop("1", "Muenchen"));
		manager.persist(new User("1", "Hans", "Mustermann"));
		manager.flush();
		manager.clear();
	}

	@Test public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class, "1").getId());
	}

	@Test public void testFindUser() {
		// TODO find User with EntityManager
		assertNotNull(super.manager.find(User.class, "1").getId());
	}

	@Test public void testFindShop() {
		// TODO find Shop with EntityManager
		assertNotNull(super.manager.find(Shop.class, "1").getId());
	}

}
