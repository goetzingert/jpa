package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import jakarta.persistence.Tuple;
import net.rentacar.dto.VehicleDTO;
import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

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
		vehicle.setLocation(new Shop("Stuttgart"));
		manager.persist(vehicle);
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
	public void testQueryWithMappingInSelect() {
		// 1. DTO-Konstruktor-Projektion: Unmanaged DTO direkt aus der Query instanziieren
		VehicleDTO dto = manager
				.createQuery(
						"SELECT new "
								+ VehicleDTO.class.getName()
								+ "(f.model.modell, f.maxKpH) FROM VehicleType f ORDER BY f.model.modell", VehicleDTO.class)
				.getResultList().get(0);

		assertNotNull(dto);
		assertTrue(dto instanceof VehicleDTO);
		assertEquals("10to", dto.modell);
		assertEquals(200, dto.maxKph);
	}

	@Test
	public void testScalarProjectionReturnsSingleColumnList() {
		// 2. Skalar-Projektion: Einzelne Spalte direkt als typisierte Liste von Strings laden
		List<String> brands = manager.createQuery(
				"SELECT DISTINCT f.model.brand FROM VehicleType f ORDER BY f.model.brand", String.class)
				.getResultList();

		assertEquals(3, brands.size());
		assertEquals("BMW", brands.get(0));
		assertEquals("Mercedes", brands.get(1));
		assertEquals("VW", brands.get(2));
	}

	@Test
	public void testMultipleScalarFieldsReturnObjectArrayList() {
		// 3. Mehrere Spalten ohne DTO: Liefert List<Object[]>
		List<Object[]> rows = manager.createQuery(
				"SELECT f.model.brand, f.hp FROM VehicleType f ORDER BY f.hp DESC", Object[].class)
				.getResultList();

		assertEquals(3, rows.size());
		Object[] firstRow = rows.get(0);
		assertEquals("BMW", firstRow[0]);
		assertEquals(150L, firstRow[1]);
	}

	@Test
	public void testTupleProjectionWithAlias() {
		// 4. JPA 2.0+ Tuple-Projektion: Typsicherer Zugriff über Spalten-Aliase
		List<Tuple> tuples = manager.createQuery(
				"SELECT f.model.brand AS brand, f.hp AS hp FROM VehicleType f ORDER BY f.hp DESC", Tuple.class)
				.getResultList();

		assertEquals(3, tuples.size());
		Tuple topVehicle = tuples.get(0);
		assertEquals("BMW", topVehicle.get("brand", String.class));
		assertEquals(150L, topVehicle.get("hp", Long.class));
	}

	@Test
	public void testDtoProjectionWithFilter() {
		// 5. DTO-Projektion kombiniert mit WHERE-Filter und Parameterbindung
		List<VehicleDTO> powerfulVehicles = manager.createQuery(
				"SELECT new " + VehicleDTO.class.getName()
						+ "(f.model.modell, f.maxKpH) FROM VehicleType f WHERE f.hp > :minHp", VehicleDTO.class)
				.setParameter("minHp", 130)
				.getResultList();

		assertEquals(1, powerfulVehicles.size());
		assertEquals("323", powerfulVehicles.get(0).modell);
		assertEquals(220, powerfulVehicles.get(0).maxKph);
	}
}
