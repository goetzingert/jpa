package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.VehicleType;
import net.rentacar.model.Vehicle;
import net.rentacar.model.Shop;
import net.rentacar.model.Truck;
import net.rentacar.model.Model;
import net.rentacar.model.Nutzer;
import net.rentacar.model.Person;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private VehicleType Truck;
	private Shop ShopMuenchen;
	private Vehicle VehicleItem;
	private Nutzer nutzer;
	private Shop stuttgart;

	@Override
	public void setUp() throws Exception {

		Truck = new Truck(new Model("VW", "Golf"), 120, 200, 10000);
		manager.persist(Truck);
		ShopMuenchen = new Shop("Muenchen");
		VehicleItem = new Vehicle(ShopMuenchen, Truck);
		stuttgart = new Shop("Stuttgart");
		VehicleItem.setLocation(stuttgart);
		manager.persist(VehicleItem);
		nutzer = new Nutzer(new Person("Hans", "Mustermann"));
		manager.persist(nutzer);
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class, Truck.getId())
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
	public void testFindPersonByNutzer() {
		assertNotNull(super.manager.find(Nutzer.class, nutzer.getId())
				.getPerson().getFirstName());
	}

	@Test
	public void testOneToManyOfShop() {
		assertTrue(super.manager.find(Shop.class, stuttgart.getId())
				.getVehicles().toArray().length > 0);
	}

	@Test
	public void testManyToOneVehicleItem() {
		assertNotNull(super.manager.find(Vehicle.class, VehicleItem.getId())
				.getLocation());
	}

	@Test
	public void testManyToManyVehicleItem() {
		assertTrue(super.manager.find(Vehicle.class, VehicleItem.getId())
				.getLocationHistory().size() > 0);
	}

	@Test
	public void testInheritance() {
		assertNotNull(super.manager.find(Truck.class, Truck.getId()));
	}
}
