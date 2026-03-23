package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.VehicleType;
import net.rentacar.model.Vehicle;
import net.rentacar.model.Shop;
import net.rentacar.model.Model;
import net.rentacar.model.Nutzer;
import net.rentacar.model.Person;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private VehicleType vehicleType;
	private Shop Shop;
	private Vehicle vehicle;
	private Nutzer nutzer;

	@Override
	public void setUp() throws Exception {

		vehicleType= new VehicleType(new Model("VW", "Golf"), 120, 200);
		manager.persist(vehicleType);
		Shop = new Shop("Muenchen");
		vehicle = new Vehicle(Shop, vehicleType);
		manager.persist(vehicle);
		nutzer = new Nutzer(new Person("Hans", "Mustermann"));
		manager.persist(nutzer);
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class, vehicle.getId())
				.getId());
	}

	@Test
	public void testFindNutzer() {
		// TODO find Nutzer with EntityManager
		assertNotNull(super.manager.find(Nutzer.class, nutzer.getId()).getId());
	}

	@Test
	public void testFindShop() {
		// TODO find Shop with EntityManager
		assertNotNull(super.manager.find(Shop.class, Shop.getId())
				.getId());
	}

	@Test
	public void testFindModel() {
		assertNotNull(super.manager.find(VehicleType.class, vehicle.getId())
				.getModel().getBrand());
	}

	@Test
	public void testFindPersonByNutzer() {
		assertNotNull(super.manager.find(Nutzer.class, nutzer.getId())
				.getPerson().getFirstName());
	}

	@Test
	public void testOneToManyOfShop() {
		assertTrue(super.manager.find(Shop.class, Shop.getId())
				.getVehicles().toArray().length > 0);
	}

	@Test
	public void testManyToOneVehicle() {
		assertNotNull(super.manager.find(Vehicle.class, vehicle.getId())
				.getLocation());
	}
}
