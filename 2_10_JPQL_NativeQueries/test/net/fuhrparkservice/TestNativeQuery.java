package net.fuhrparkservice;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestNativeQuery extends AbstractJPATestCase {

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
		Shop stuttgart = new Shop("Stuttgart");
		vehicleItem.setLocation(stuttgart);
		manager.persist(vehicleItem);
		Vehicle vehicleItem2 = new Vehicle(stuttgart, Vehicle3);
		manager.persist(vehicleItem2);
		manager.persist(new Shop("Koeln"));
		Customer customer = new Customer(new Person("Hans", "Mustermann"));
		manager.persist(customer);
		Customer customer2 = new Customer(new Person("Franz", "Mueller"));
		manager.persist(customer2);
		manager.persist(new Customer(new Person("Herbert", "Schmitt")));
		manager.persist(new Customer(new Person("Ingo", "Meyer")));
		manager.persist(new Customer(new Person("Mathias", "Mayer")));
		manager.persist(new Customer(new Person("Michael", "Anstaedt")));
		manager.persist(new Customer(new Person("Ralf", "Gross")));
		
		manager.flush();
		manager.clear();
	}

	@Test public void testNativeQueryOfKunde() {
		// TODO Lade alle Kunden- und Personendaten mittels Native Query
	}

	@Test public void testNativeQueryOfVehicleUndItem() {
		// TODO Lade alle Vehicles mit brand BMW und zugehˆrige VehicleItem
		// mittels Native Query und ResultSetMapping
	}

}
