package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		
		VehicleType Vehicle = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(Vehicle);
		VehicleType Vehicle2 = new Truck( new Model("Mercedes", "10to"), 120,
				200, 10000);
		manager.persist(Vehicle2);
		VehicleType Vehicle3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(Vehicle3);
		Shop shop = new Shop( "Muenchen");
		Vehicle vehicle = new Vehicle(shop, Vehicle);
		vehicle.setLocation(new Shop( "Stuttgart"));
		manager.persist(vehicle);
		manager.persist(new User( new Person("Hans", "Mustermann")));
		manager.flush();
		manager.clear();
	}

	@Test
	public void findsOnlyBmwVehicleTypeAbove130Horsepower() {
		List<VehicleType> result = manager.createQuery(
				"SELECT f FROM VehicleType f WHERE f.hp > 130", VehicleType.class)
				.getResultList();

		assertEquals(1, result.size());
		VehicleType vehicleType = result.get(0);
		assertNotNull(vehicleType.getModel());
		assertEquals("BMW", vehicleType.getModel().getBrand());
		assertEquals(150, vehicleType.getHp());
	}

	@Test
	public void returnsAllVehicleTypesPolymorphically() {
		// JPQL ist standardmäßig polymorph: Liefert Instanzen von Car und Truck
		List<VehicleType> allTypes = manager.createQuery(
				"SELECT v FROM VehicleType v ORDER BY v.model.brand", VehicleType.class)
				.getResultList();

		assertEquals(3, allTypes.size());
		assertEquals("BMW", allTypes.get(0).getModel().getBrand());
		assertInstanceOf(Car.class, allTypes.get(0));
		assertEquals("Mercedes", allTypes.get(1).getModel().getBrand());
		assertInstanceOf(Truck.class, allTypes.get(1));
		assertEquals("VW", allTypes.get(2).getModel().getBrand());
		assertInstanceOf(Car.class, allTypes.get(2));
	}

	@Test
	public void filtersBySpecificSubtypeUsingTypeFunction() {
		// TYPE()-Funktion filtert gezielt auf konkrete Unterklassen in Vererbungshierarchien
		List<VehicleType> carsOnly = manager.createQuery(
				"SELECT v FROM VehicleType v WHERE TYPE(v) = Car", VehicleType.class)
				.getResultList();

		assertEquals(2, carsOnly.size());
		for (VehicleType vt : carsOnly) {
			assertInstanceOf(Car.class, vt);
		}
	}

	@Test
	public void findsSingleVehicleTypeByExactBrand() {
		// getSingleResult() liefert direkt die Entity, wenn genau 1 Treffer erwartet wird
		VehicleType bmw = manager.createQuery(
				"SELECT v FROM VehicleType v WHERE v.model.brand = 'BMW'", VehicleType.class)
				.getSingleResult();

		assertNotNull(bmw);
		assertEquals("BMW", bmw.getModel().getBrand());
		assertEquals(150, bmw.getHp());
	}

	@Test
	public void throwsNoResultExceptionWhenBrandNotFound() {
		// getSingleResult() wirft NoResultException bei 0 Treffern (statt null zurückzugeben!)
		TypedQuery<VehicleType> query = manager.createQuery(
				"SELECT v FROM VehicleType v WHERE v.model.brand = 'Porsche'", VehicleType.class);

		assertThrows(NoResultException.class, query::getSingleResult);
	}

	@Test
	public void throwsNonUniqueResultExceptionWhenMultipleCarsMatch() {
		// getSingleResult() wirft NonUniqueResultException bei mehr als 1 Treffer
		TypedQuery<VehicleType> query = manager.createQuery(
				"SELECT v FROM VehicleType v WHERE TYPE(v) = Car", VehicleType.class);

		assertThrows(NonUniqueResultException.class, query::getSingleResult);
	}
}
