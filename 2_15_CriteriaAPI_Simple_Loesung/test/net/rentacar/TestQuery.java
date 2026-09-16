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
		manager.persist(new User(new Person("Michael", "Anstädt")));
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

		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> user = cq.from(User.class);

		cq.select(user).where(user.get("person").get("firstName").in(list));

		assertEquals(2, manager.createQuery(cq).getResultList().size());
	}

	@Test
	public void testQueryForShopMitKeinemVehicle() {
		// 2. IS EMPTY-Bedingung auf Collection-Assoziationen (cb.isEmpty)
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Shop> cq = cb.createQuery(Shop.class);
		Root<Shop> shop = cq.from(Shop.class);

		cq.select(shop).where(cb.isEmpty(shop.get("carpool")));

		assertEquals(2, manager.createQuery(cq).getResultList().size());
	}

	@Test
	public void testQueryForShopsichtInStuttgartUndMuenchen_MitINOperator() {
		// 3. NOT IN-Filterung mit CriteriaBuilder (cb.not(...in...))
		List<String> orte = Arrays.asList("Muenchen", "Stuttgart");

		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Shop> cq = cb.createQuery(Shop.class);
		Root<Shop> shop = cq.from(Shop.class);

		cq.select(shop).where(cb.not(shop.get("location").in(orte)));

		assertEquals(1, manager.createQuery(cq).getResultList().size());
	}

	@Test
	public void criteriaQueryCanApplyStableOrdering() {
		// 4. Deterministische Sortierung mit cb.asc() / cb.desc()
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> user = cq.from(User.class);

		cq.select(user).orderBy(cb.asc(user.get("person").get("firstName")));

		assertEquals(7, manager.createQuery(cq).getResultList().size());
	}

	@Test
	public void testCriteriaNumericComparisonAndConjunction() {
		// 5. Numerische Vergleiche und Konjunktion (cb.and, cb.equal, cb.ge)
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<VehicleType> cq = cb.createQuery(VehicleType.class);
		Root<VehicleType> root = cq.from(VehicleType.class);

		cq.select(root).where(
				cb.and(
						cb.equal(root.get("model").get("brand"), "VW"),
						cb.ge(root.get("hp"), 120L)
				)
		);

		List<VehicleType> results = manager.createQuery(cq).getResultList();
		assertEquals(1, results.size());
		assertEquals("Golf", results.get(0).getModel().getModell());
	}

	@Test
	public void testCriteriaJoinAcrossEntities() {
		// 6. Expliziter Join über Entity-Assoziationen (root.join("type"))
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Vehicle> cq = cb.createQuery(Vehicle.class);
		Root<Vehicle> vehicleRoot = cq.from(Vehicle.class);
		Join<Vehicle, VehicleType> typeJoin = vehicleRoot.join("type");

		cq.select(vehicleRoot).where(cb.equal(typeJoin.get("model").get("brand"), "VW"));

		List<Vehicle> vehicles = manager.createQuery(cq).getResultList();
		assertEquals(1, vehicles.size());
		assertNotNull(vehicles.get(0).getType());
	}

	@Test
	public void testCriteriaAggregateCalculations() {
		// 7. Aggregatfunktionen via Criteria API (COUNT, AVG, MAX mit multiselect)
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<VehicleType> root = cq.from(VehicleType.class);

		cq.multiselect(cb.count(root), cb.avg(root.get("hp")), cb.max(root.get("maxKpH")));

		Object[] result = manager.createQuery(cq).getSingleResult();
		assertEquals(3L, result[0]);
		assertEquals(130.0, ((Number) result[1]).doubleValue(), 0.01);
		assertEquals(220L, result[2]);
	}

	@Test
	public void testCriteriaDynamicQueryWithPredicateList() {
		// 8. Dynamischer Query-Aufbau mit Predicate-Liste (praxisnah für flexible Suchmasken)
		String filterBrand = "BMW";
		Long minHp = 130L;

		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<VehicleType> cq = cb.createQuery(VehicleType.class);
		Root<VehicleType> root = cq.from(VehicleType.class);

		List<Predicate> predicates = new ArrayList<>();
		if (filterBrand != null && !filterBrand.isEmpty()) {
			predicates.add(cb.equal(root.get("model").get("brand"), filterBrand));
		}
		if (minHp != null) {
			predicates.add(cb.ge(root.get("hp"), minHp));
		}

		cq.select(root).where(predicates.toArray(new Predicate[0]));

		List<VehicleType> results = manager.createQuery(cq).getResultList();
		assertEquals(1, results.size());
		assertEquals("323", results.get(0).getModel().getModell());
	}
}
