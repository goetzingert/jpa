package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestNativeQuery extends AbstractJPATestCase {

	private Vehicle vehicle;

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
		Shop stuttgart = new Shop("Stuttgart");
		vehicle.setLocation(stuttgart);
		manager.persist(vehicle);
		Vehicle vehicle2 = new Vehicle(stuttgart, Vehicle3);
		manager.persist(vehicle2);
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
		// 1. Native SQL direkt auf Entity-Klasse mappen (liefert gemanagte Customer-Entities)
		List<Customer> customers = null; // TODO: Native Query mit "SELECT * FROM tbl_User WHERE DTYPE = 'Customer' ORDER BY id" und Customer.class

		assertNotNull(customers);
		assertEquals(7, customers.size());
		assertNotNull(customers.get(0).getPerson());
		assertEquals("Hans", customers.get(0).getPerson().getFirstName());
	}

	@Test
	public void testNativeQueryOfVehicleUndItem() {
		// 2. Komplexe relationale Joins über Vererbungs- und Assoziationstabellen mit Native SQL
		List<?> vehicleTypes = null; // TODO: "SELECT t.id, t.modell, t.brand, t.HP, t.maxKph, p.doors, l.maxLoad FROM tbl_VehicleType t LEFT OUTER JOIN TBL_Car p ON t.id = p.id LEFT OUTER JOIN TBL_TRUCK l ON t.id = l.id"
		assertNotNull(vehicleTypes);
		assertFalse(vehicleTypes.isEmpty());
		assertEquals(3, vehicleTypes.size());

		List<?> resultList = null; // TODO: Native Query mit JOIN auf tbl_Vehicle (i.type_id = t.id)
		assertNotNull(resultList);
		assertFalse(resultList.isEmpty());
		assertEquals(2, resultList.size());
	}

	@Test
	public void nativeQuerySupportsBoundParameters() {
		// 3. Positions-Parameter (?1) in Native Queries verwenden
		Number count = null; // TODO: Native Query "SELECT COUNT(*) FROM tbl_VehicleType WHERE HP > ?1" mit Parameter 1 = 130
		assertNotNull(count);
		assertEquals(1, count.intValue());
	}

	@Test
	public void testNativeQueryScalarAggregates() {
		// 4. Skalare Aggregatfunktionen über Native SQL ausführen (liefert Object[])
		Object[] agg = null; // TODO: Native Query "SELECT COUNT(*), AVG(HP), MAX(maxKph), MIN(HP) FROM tbl_VehicleType" mit getSingleResult()
		assertNotNull(agg);
		assertEquals(3, ((Number) agg[0]).intValue());
		assertEquals(130.0, ((Number) agg[1]).doubleValue(), 0.01);
		assertEquals(220, ((Number) agg[2]).intValue());
		assertEquals(120, ((Number) agg[3]).intValue());
	}

	@Test
	public void testNativeQueryPagination() {
		// 5. Deterministisches Paging mit setFirstResult und setMaxResults auf Native Queries
		List<?> page = null; // TODO: Native Query mit setFirstResult(2) und setMaxResults(3)
		assertNotNull(page);
		assertEquals(3, page.size());
	}

	@Test
	public void testNativeQueryDmlUpdateAndClearSync() {
		// 6. Native DML-Updates (executeUpdate) und Notwendigkeit von manager.clear()
		int updated = 0; // TODO: manager.createNativeQuery("UPDATE tbl_VehicleType SET HP = HP + 10 WHERE brand = 'BMW'").executeUpdate();
		assertEquals(1, updated);

		// TODO: manager.clear() aufrufen, um den Persistence Context zu synchronisieren

		VehicleType bmw = manager.createQuery(
				"SELECT v FROM VehicleType v WHERE v.model.brand = 'BMW'", VehicleType.class)
				.getSingleResult();
		assertEquals(160, bmw.getHp());
	}

}
