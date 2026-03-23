package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	private Vehicle vehicle;

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
		vehicle = new Vehicle(shop, Vehicle);
		vehicle.setLocation(new Shop("Stuttgart"));
		manager.persist(vehicle);
		manager.persist(new User(new Person("Hans", "Mustermann")));
		manager.persist(new User(new Person("Franz", "Mueller")));
		manager.persist(new User(new Person("Herbert", "Schmitt")));
		manager.persist(new User(new Person("Ingo", "Meyer")));
		manager.persist(new User(new Person("Mathias", "Mayer")));
		manager.persist(new User(new Person("Michael", "Anstädt")));
		manager.persist(new User(new Person("Ralf", "Gross")));

		Shop koeln = new Shop("Köln");
		manager.persist(koeln);

		manager.flush();
		manager.clear();
	}

	@Test
	public void testQueryForNutzerWithPersonnameISMichaelOrMathias() {
		List<String> list = Arrays.asList("Michael", "Mathias");

		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> user = cq.from(User.class);

		cq.select(user).where(user.get("person").get("firstName").in(list));

		assertEquals(2, manager.createQuery(cq).getResultList().size());
	}

	@Test
	public void testQueryForShopMitKeinemVehicle() {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Shop> cq = cb.createQuery(Shop.class);
		Root<Shop> shop = cq.from(Shop.class);

		cq.select(shop).where(cb.isEmpty(shop.get("carpool")));

		assertEquals(2, manager.createQuery(cq).getResultList().size());
	}

	@Test
	public void testQueryForShopsichtInStuttgartUndMuenchen_MitINOperator() {
		List<String> orte = Arrays.asList("Muenchen", "Stuttgart");

		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Shop> cq = cb.createQuery(Shop.class);
		Root<Shop> shop = cq.from(Shop.class);

		cq.select(shop).where(cb.not(shop.get("location").in(orte)));

		assertEquals(1, manager.createQuery(cq).getResultList().size());
	}
}
