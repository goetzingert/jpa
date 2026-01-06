package net.fuhrparkservice;

import java.util.List;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	private Vehicle vehicleItemVW;

	public void setUp() throws Exception {

		VehicleType vehicleTypeVW = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(vehicleTypeVW);
		VehicleType Vehicle2 = new Truck(new Model("Mercedes", "10to"), 120,
				200, 10000);
		manager.persist(Vehicle2);
		VehicleType Vehicle3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(Vehicle3);
		Shop shop = new Shop("Muenchen");
		Vehicle vehicleBMW = new Vehicle(shop,Vehicle3);
		manager.persist(vehicleBMW);
		vehicleItemVW = new Vehicle(shop, vehicleTypeVW);
		vehicleItemVW.setLocation(new Shop("Stuttgart"));
		manager.persist(vehicleItemVW);
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
	public void testForVehicleItemInShop() throws InterruptedException {
		List<Shop> ShoHP = manager.createQuery("FROM Shop f LEFT JOIN FETCH f.carpool fah LEFT JOIN FETCH fah.type")
				.getResultList();
		System.out.println("Shop geholt");
		for (Shop shop : ShoHP) {
		
			for (Vehicle item : shop.getCarpool()) {
				System.out.println(item.getType().getModel().getBrand());
			}
		}
	}

}
