package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	private Vehicle vehicleItem;

	@Override
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
		manager.persist(vehicleItem);
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
	public void testQueryForNutzerWithPersonnameISMichaelOrMathias() {
		List<String> list = Arrays.asList("Michael","Mathias");
		assertEquals(
				2,
				manager.createQuery(
						"SELECT n FROM User n WHERE n.person.firstName IN :FIRSTNAMEn")
						.setParameter("FIRSTNAMEn", list).getResultList().size());
	}

	@Test
	public void testQueryForShopMitKeinemVehicle() {
		assertEquals(
				2,
				manager.createQuery(
						"SELECT f FROM Shop f WHERE f.carpool IS EMPTY")
						.getResultList().size());
	}

	@Test
	public void testQueryForShopsichtInStuttgartUndMuenchen_MitINOperator() {
		List<String> orte = Arrays.asList("Muenchen","Stuttgart");
		assertEquals(
				1,
				manager.createQuery(
						"SELECT f FROM Shop f WHERE f.location NOT IN :orte")
						.setParameter("orte", orte).getResultList().size());
	}
}
