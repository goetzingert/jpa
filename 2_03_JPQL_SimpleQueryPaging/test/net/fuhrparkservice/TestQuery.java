package net.fuhrparkservice;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	public void setUp() throws Exception {

		VehicleType Vehicle = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(Vehicle);
		VehicleType Vehicle2 = new Truck(new Model("Mercedes", "10to"), 120,
				200, 10000);
		manager.persist(Vehicle2);
		VehicleType Vehicle3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(Vehicle3);
		Shop shop = new Shop("Muenchen");
		Vehicle vehicleItem = new Vehicle(shop, Vehicle);
		vehicleItem.setLocation(new Shop("Stuttgart"));
		manager.persist(vehicleItem);
		manager.persist(new User(new Person("Hans", "Mustermann")));
		manager.persist(new User(new Person("Franz", "Mueller")));
		manager.persist(new User(new Person("Herbert", "Schmitt")));
		manager.persist(new User(new Person("Ingo", "Meyer")));
		manager.persist(new User(new Person("Mathias", "Mayer")));
		manager.persist(new User(new Person("Michael", "Anstaedt")));
		manager.persist(new User(new Person("Ralf", "Gross")));
		manager.flush();
		manager.clear();
	}

	@Test
	public void testPagingBySelectNutzer() {
		// SELECT auf Nutzer mit PAGE-SIZE 3
	}
}
