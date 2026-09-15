package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import net.rentacar.model.Vehicle;
import net.rentacar.model.VehicleType;
import net.rentacar.model.Shop;
import net.rentacar.model.Truck;
import net.rentacar.model.Model;
import net.rentacar.model.User;
import net.rentacar.model.Person;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private VehicleType Truck;
	private Shop ShopMuenchen;
	private Vehicle Vehicle;
	private User user;
	private Shop stuttgart;

	@Override
	public void setUp() throws Exception {

		Truck = new Truck(new Model("VW", "Golf"), 120, 200, 10000);
		manager.persist(Truck);
		ShopMuenchen = new Shop("Muenchen");
		Vehicle = new Vehicle(ShopMuenchen, Truck);
		stuttgart = new Shop("Stuttgart");
		Vehicle.setLocation(stuttgart);
		manager.persist(Vehicle);
		user = new User(new Person("Hans", "Mustermann"));
		manager.persist(user);
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class, Truck.getId())
				.getId());
	}

	@Test
	public void testFindUser() {
		// TODO find User with EntityManager
		assertNotNull(super.manager.find(User.class, user.getId()).getId());
	}

	@Test
	public void testFindShop() {
		// TODO find Shop with EntityManager
		assertNotNull(super.manager
				.find(Shop.class, ShopMuenchen.getId()).getId());
	}

	@Test
	public void testFindModel() {
		assertNotNull(super.manager
				.find(VehicleType.class, Truck.getId()).getModel()
				.getBrand());
	}

	@Test
	public void testFindPersonByUser() {
		assertNotNull(super.manager.find(User.class, user.getId())
				.getPerson().getFirstName());
	}

	@Test
	public void testOneToManyOfShop() {
		assertTrue(super.manager.find(Shop.class, stuttgart.getId())
				.getVehicles().toArray().length > 0);
	}

	@Test
	public void testManyToOneVehicle() {
		assertNotNull(super.manager.find(Vehicle.class, Vehicle.getId())
				.getLocation());
	}

	@Test
	public void testManyToManyVehicle() {
		assertTrue(super.manager.find(Vehicle.class, Vehicle.getId())
				.getLocationHistory().size() > 0);
	}

	@Test
	public void testInheritance() {
		assertNotNull(super.manager.find(Truck.class, Truck.getId()));
	}
}
