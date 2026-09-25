package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.VehicleType;
import net.rentacar.model.Shop;
import net.rentacar.model.User;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private VehicleType vehicleType;
	private Shop shop;
	private User user;

	@Override
	public void setUp() throws Exception {
		vehicleType = new VehicleType("VW", "Golf", 120, 200);
		shop = new Shop("Muenchen");
		user = new User("Hans", "Mustermann");
		manager.persist(vehicleType);
		manager.persist(shop);
		manager.persist(user);
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class, vehicleType.getId()).getId());
	}

	@Test
	public void testFindUser() {
		// TODO find User with EntityManager
		assertNotNull(super.manager.find(User.class, user.getId()).getId());
	}

	@Test
	public void testFindShop() {
		// TODO find Shop with EntityManager
		assertNotNull(super.manager.find(Shop.class, shop.getId()).getId());
	}

	@Test
	public void testFindModel() {
		//assertNotNull(super.manager.find(VehicleType.class, vehicleType.getId()).getModell()
		//		.getBrand());
	}

}
