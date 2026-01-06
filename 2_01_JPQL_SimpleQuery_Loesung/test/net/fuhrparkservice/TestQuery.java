package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	public void setUp() throws Exception {
		
		VehicleType Vehicle = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(Vehicle);
		VehicleType Vehicle2 = new Truck( new Model("Mercedes", "10to"), 120,
				200, 10000);
		manager.persist(Vehicle2);
		VehicleType Vehicle3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(Vehicle3);
		Shop shop = new Shop( "Muenchen");
		Vehicle vehicleItem = new Vehicle(shop, Vehicle);
		vehicleItem.setLocation(new Shop( "Stuttgart"));
		manager.persist(vehicleItem);
		manager.persist(new User( new Person("Hans", "Mustermann")));
		manager.flush();
		manager.clear();
	}

	@Test public void testQueryForVehicleMoreThan130HP() {
		assertEquals(1, manager.createQuery("SELECT f FROM VehicleType f WHERE f.hp > 130")
				.getResultList().size());
	}
}
