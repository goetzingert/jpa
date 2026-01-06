package net.fuhrparkservice;

import java.util.List;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	private Vehicle vehicleItem;

	public void setUp() throws Exception {

		VehicleType Vehicle = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(Vehicle);
		VehicleType Vehicle2 = new Truck(new Model("Mercedes", "10to"), 120,
				200, 10000);
		manager.persist(Vehicle2);
		VehicleType Vehicle3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(Vehicle3);
		Shop shop = new Shop("Muenchen");
		vehicleItem = new Vehicle(shop, Vehicle);
		vehicleItem.setLocation(new Shop("Stuttgart"));
		
		Vehicle vehicleItem2 = new Vehicle(shop, Vehicle2);
		manager.persist(vehicleItem2);
		manager.persist(new User(new Person("Hans", "Mustermann")));
		manager.persist(new User(new Person("Franz", "Mueller")));
		manager.persist(new User(new Person("Herbert", "Schmitt")));
		manager.persist(new User(new Person("Ingo", "Meyer")));
		manager.persist(new User(new Person("Mathias", "Mayer")));
		manager.persist(new User(new Person("Michael", "Anst�dt")));
		manager.persist(new User(new Person("Ralf", "Gross")));

		Shop koeln = new Shop("K�ln");
		manager.persist(koeln);

		manager.flush();
		manager.clear();
		
	}

	@Test
	public void testForVehicleItemInShop() {
		List<Shop> shop = manager
				.createQuery(
						"Select f FROM Filliale f LEFT JOIN FETCH f.Vehicles", Shop.class)
				.getResultList();
		for (Shop shop2 : shop) {
			
			for (Vehicle item : shop2.getCarpool()) {
				System.out.println(item.getType().getModel().getBrand());
			}
		}
	}

}
