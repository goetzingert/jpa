package net.fuhrparkservice;

import java.util.List;

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


	@Test
	public void testNativeQueryOfKunde() {
		// TODO Lade alle Kunden- und Personendaten mittels Native Query
	}

	@Test
	public void testNativeQueryOfVehicleUndItem() {
		// TODO Lade alle Vehicles mit brand BMW und zugeh�rige VehicleItem
		// mittels Native Query und ResultSetMapping
		System.out.println(manager.createNativeQuery(
				"Select t.id, t.modell, t.brand, t.HP, t.maxKph, p.doors, l.maxLoad "
						+ "FROM tbl_VehicleType t LEFT OUTER JOIN TBL_Car p ON t.id = p.id LEFT OUTER JOIN TBL_TRUCK l ON t.id = l.id",
				"VehicleType_Mapping").getResultList());

		List<?> resultList = manager.createNativeQuery(
				"Select t.id, t.modell, t.brand, t.HP, t.maxKph, p.doors, l.maxLoad, i.id AS item_id, i.type_id, i.LOCATION_ID "
						+ "FROM tbl_VehicleType t LEFT OUTER JOIN TBL_Car p ON t.id = p.id LEFT OUTER JOIN TBL_TRUCK l ON t.id = l.id JOIN tbl_Vehicle i ON i.type_id = t.id",
				"VehicleType_Item_Mapping").getResultList();
		System.out.println(resultList);
	}

}
