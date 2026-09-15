package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.VehicleType;
import net.rentacar.model.Vehicle;
import net.rentacar.model.Shop;
import net.rentacar.model.Truck;
import net.rentacar.model.Model;
import net.rentacar.model.User;
import net.rentacar.model.Person;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private VehicleType truck;
	private Shop shopMuenchen;
	private Vehicle vehicle;
	private User user;
	private Shop stuttgart;

	@Override
	public void setUp() throws Exception {

		truck = new Truck(new Model("VW", "Golf"), 120, 200, 10000);
		manager.persist(truck);
		shopMuenchen = new Shop("Muenchen");
		vehicle = new Vehicle(shopMuenchen, truck);
		stuttgart = new Shop("Stuttgart");
		vehicle.setLocation(stuttgart);
		manager.persist(vehicle);
		user = new User(new Person("Hans", "Mustermann"));
		manager.persist(user);
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class, truck.getId())
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
				.find(Shop.class, shopMuenchen.getId()).getId());
	}

	@Test
	public void testFindModel() {
		assertNotNull(super.manager
				.find(VehicleType.class, truck.getId()).getModel()
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
		assertNotNull(super.manager.find(Vehicle.class, vehicle.getId())
				.getLocation());
	}

	@Test
	public void testManyToManyVehicle() {
		assertTrue(super.manager.find(Vehicle.class, vehicle.getId())
				.getLocationHistory().size() > 0);
	}

	@Test
	public void testInheritance() {
		assertNotNull(super.manager.find(Truck.class, truck.getId()));
	}
}
