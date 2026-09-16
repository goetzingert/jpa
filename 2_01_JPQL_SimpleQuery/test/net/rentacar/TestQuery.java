package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;

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
	public void testQueryForVehicleTypeMoreThan130HP() {
		// 1. Erstelle eine typisierte Abfrage für alle Fahrzeugtypen mit mehr als 130 PS
		TypedQuery<VehicleType> query = null; // TODO: manager.createQuery(..., VehicleType.class);
		List<VehicleType> resultList = query != null ? query.getResultList() : null;

		assertNotNull(resultList);
		assertEquals(1, resultList.size());
		assertEquals("BMW", resultList.get(0).getModel().getBrand());
	}

	@Test
	public void testPolymorphicQueryReturnsAllSubtypes() {
		// 2. Polymorphe Abfrage: JPQL "SELECT v FROM VehicleType v" muss sowohl Car als auch Truck liefern
		List<VehicleType> allTypes = null; // TODO: Query formulieren und sortiert nach v.model.brand ausführen

		assertNotNull(allTypes);
		assertEquals(3, allTypes.size());
		assertInstanceOf(Car.class, allTypes.get(0)); // BMW
		assertInstanceOf(Truck.class, allTypes.get(1)); // Mercedes
		assertInstanceOf(Car.class, allTypes.get(2)); // VW
	}

	@Test
	public void testFilterBySpecificSubtypeUsingTypeFunction() {
		// 3. Verwende die JPQL-Funktion TYPE(v) = Car, um gezielt nur PKWs abzufragen
		List<VehicleType> carsOnly = null; // TODO: Query mit WHERE TYPE(v) = Car

		assertNotNull(carsOnly);
		assertEquals(2, carsOnly.size());
		for (VehicleType vt : carsOnly) {
			assertInstanceOf(Car.class, vt);
		}
	}

	@Test
	public void testFindSingleVehicleTypeByExactBrand() {
		// 4. Eindeutige Abfrage: Nutze getSingleResult() für den BMW
		VehicleType bmw = null; // TODO: Query für Marke 'BMW' mit getSingleResult() ausführen

		assertNotNull(bmw);
		assertEquals("BMW", bmw.getModel().getBrand());
	}

	@Test
	public void testGetSingleResultThrowsNoResultExceptionWhenNotFound() {
		// 5. Randfall: Was passiert, wenn eine Query mit getSingleResult() keinen Treffer liefert?
		// Formuliere eine Query nach Marke 'Porsche' und überprüfe, dass NoResultException geworfen wird.
		TypedQuery<VehicleType> query = null; // TODO

		assertNotNull(query);
		assertThrows(NoResultException.class, query::getSingleResult);
	}

	@Test
	public void testGetSingleResultThrowsNonUniqueResultExceptionWhenMultipleMatches() {
		// 6. Randfall: Was passiert bei getSingleResult(), wenn mehrere Treffer existieren?
		// Formuliere eine Query für alle PKWs (TYPE(v) = Car) und prüfe auf NonUniqueResultException.
		TypedQuery<VehicleType> query = null; // TODO

		assertNotNull(query);
		assertThrows(NonUniqueResultException.class, query::getSingleResult);
	}
}
