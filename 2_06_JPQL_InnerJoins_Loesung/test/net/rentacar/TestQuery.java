package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	private Vehicle vehicle;

	public void setUp()  {

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

		Shop koeln = new Shop("K�ln");
		manager.persist(koeln);

		manager.flush();
		manager.clear();
	}

	@Test
	public void testInnerJoin_SearchShopWithVehicleOfBrandVW() {
		// 1. Expliziter INNER JOIN über Collection-Assoziation f.carpool
		Shop shop = manager
				.createQuery(
						"SELECT f FROM Shop f INNER JOIN f.carpool fz WHERE fz.type.model.brand = 'VW'", Shop.class)
				.getSingleResult();

		assertNotNull(shop);
		assertEquals("Stuttgart", shop.getLocation());
	}

	@Test
	public void testIn_SearchShopWithVehicleOfBrandVW() {
		// 2. Alternative IN()-Syntax für Collection-Navigation
		Shop shop = manager
				.createQuery(
						"SELECT f FROM Shop f, IN (f.carpool) fz WHERE fz.type.model.brand = 'VW'", Shop.class)
				.getSingleResult();

		assertNotNull(shop);
		assertEquals("Stuttgart", shop.getLocation());
	}

	@Test
	public void leftJoinAlsoReturnsShopsWithoutVehicles() {
		// 3. LEFT JOIN: Liefert alle Shops, auch diejenigen ohne Fahrzeuge (Köln & München)
		List<Shop> shops = manager.createQuery(
				"SELECT DISTINCT f FROM Shop f LEFT JOIN f.carpool fz", Shop.class)
				.getResultList();

		assertEquals(3, shops.size());
	}

	@Test
	public void testLeftJoinWithOnClauseVsWhereClause() {
		// 4. JPA 2.1 ON-Klausel vs. WHERE-Klausel bei Outer Joins:
		// Eine ON-Bedingung schränkt nur die rechte Seite ein, der LEFT JOIN bleibt erhalten (3 Shops).
		List<Shop> onShops = manager.createQuery(
				"SELECT DISTINCT s FROM Shop s LEFT JOIN s.carpool v ON v.type.model.brand = 'BMW'", Shop.class)
				.getResultList();
		assertEquals(3, onShops.size());

		// Eine WHERE-Bedingung auf die rechte Seite filtert NULL-Werte heraus und macht den LEFT JOIN effektiv zum INNER JOIN (0 Shops).
		List<Shop> whereShops = manager.createQuery(
				"SELECT DISTINCT s FROM Shop s LEFT JOIN s.carpool v WHERE v.type.model.brand = 'BMW'", Shop.class)
				.getResultList();
		assertEquals(0, whereShops.size());
	}

	@Test
	public void testMultipleJoinsChainAcrossAssociations() {
		// 5. Mehrstufiger Join über mehrere Assoziationen (Shop -> Vehicle -> VehicleType)
		List<Shop> shopsWithPowerfulVehicles = manager.createQuery(
				"SELECT DISTINCT s FROM Shop s INNER JOIN s.carpool v INNER JOIN v.type t WHERE t.hp >= 120", Shop.class)
				.getResultList();

		assertEquals(1, shopsWithPowerfulVehicles.size());
		assertEquals("Stuttgart", shopsWithPowerfulVehicles.get(0).getLocation());
	}

	@Test
	public void testThetaJoinWithoutDirectAssociation() {
		// 6. Theta-Join (kartesisches Produkt mit Filter): Verknüpfung zweier Entities ohne Assoziation
		List<Object[]> pairs = manager.createQuery(
				"SELECT s.location, u.person.lastName FROM Shop s, User u WHERE s.location LIKE 'M%' AND u.person.lastName LIKE 'M%'", Object[].class)
				.getResultList();

		// Shop Muenchen kombiniert mit Mustermann, Mueller, Meyer, Mayer = 4 Treffer
		assertEquals(4, pairs.size());
		for (Object[] pair : pairs) {
			assertEquals("Muenchen", pair[0]);
			assertNotNull(pair[1]);
		}
	}
}
