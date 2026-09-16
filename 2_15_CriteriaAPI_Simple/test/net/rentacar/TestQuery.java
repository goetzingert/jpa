package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
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
		manager.persist(new User(new Person("Michael", "Anstaedt")));
		manager.persist(new User(new Person("Ralf", "Gross")));

		Shop koeln = new Shop("Köln");
		manager.persist(koeln);

		manager.flush();
		manager.clear();
	}

	@Test
	public void testQueryForUserWithPersonnameISMichaelOrMathias() {
		// 1. IN-Bedingung über Criteria API (user.person.firstName IN (:list))
		List<String> list = Arrays.asList("Michael", "Mathias");
		List<User> users = null; // TODO: CriteriaQuery mit user.get("person").get("firstName").in(list)

		assertNotNull(users);
		assertEquals(2, users.size());
	}

	@Test
	public void testQueryForShopMitKeinemVehicle() {
		// 2. IS EMPTY-Bedingung auf Collection-Assoziationen (cb.isEmpty)
		List<Shop> emptyShops = null; // TODO: CriteriaQuery mit cb.isEmpty(shop.get("carpool"))

		assertNotNull(emptyShops);
		assertEquals(2, emptyShops.size());
	}

	@Test
	public void testQueryForShopsichtInStuttgartUndMuenchen_MitINOperator() {
		// 3. NOT IN-Filterung mit CriteriaBuilder (cb.not(...in...))
		List<String> orte = Arrays.asList("Muenchen", "Stuttgart");
		List<Shop> otherShops = null; // TODO: CriteriaQuery mit cb.not(shop.get("location").in(orte))

		assertNotNull(otherShops);
		assertEquals(1, otherShops.size());
	}

	@Test
	public void criteriaQueryCanApplyStableOrdering() {
		// 4. Deterministische Sortierung mit cb.asc() / cb.desc()
		List<User> sortedUsers = null; // TODO: CriteriaQuery mit cq.orderBy(cb.asc(user.get("person").get("firstName")))

		assertNotNull(sortedUsers);
		assertEquals(7, sortedUsers.size());
	}

	@Test
	public void testCriteriaNumericComparisonAndConjunction() {
		// 5. Numerische Vergleiche und Konjunktion (cb.and, cb.equal, cb.ge)
		List<VehicleType> results = null; // TODO: CriteriaQuery mit cb.and(cb.equal(root.get("model").get("brand"), "VW"), cb.ge(root.get("hp"), 120L))

		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals("Golf", results.get(0).getModel().getModell());
	}

	@Test
	public void testCriteriaJoinAcrossEntities() {
		// 6. Expliziter Join über Entity-Assoziationen (root.join("type"))
		List<Vehicle> vehicles = null; // TODO: CriteriaQuery über Vehicle mit Join auf type und Filter brand = 'VW'

		assertNotNull(vehicles);
		assertEquals(1, vehicles.size());
		assertNotNull(vehicles.get(0).getType());
	}

	@Test
	public void testCriteriaAggregateCalculations() {
		// 7. Aggregatfunktionen via Criteria API (COUNT, AVG, MAX mit multiselect)
		Object[] result = null; // TODO: CriteriaQuery<Object[]> mit cq.multiselect(cb.count(root), cb.avg(root.get("hp")), cb.max(root.get("maxKpH")))

		assertNotNull(result);
		assertEquals(3L, result[0]);
		assertEquals(130.0, ((Number) result[1]).doubleValue(), 0.01);
		assertEquals(220L, result[2]);
	}

	@Test
	public void testCriteriaDynamicQueryWithPredicateList() {
		// 8. Dynamischer Query-Aufbau mit Predicate-Liste (praxisnah für flexible Suchmasken)
		String filterBrand = "BMW";
		Long minHp = 130L;

		List<VehicleType> results = null; // TODO: Baue dynamisch List<Predicate> mit optionalen Filtern für filterBrand und minHp und übergebe predicates.toArray(new Predicate[0]) an cq.where()

		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals("323", results.get(0).getModel().getModell());
	}
}
