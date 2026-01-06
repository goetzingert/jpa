package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import jakarta.persistence.Query;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	public void setUp() throws Exception {
		
		VehicleType Vehicle = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(Vehicle);
		VehicleType Vehicle2 = new Truck(new Model("Mercedes", "10to"), 120,
				200, 10000);
		manager.persist(Vehicle2);
		VehicleType Vehicle3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(Vehicle3);
		Shop shop = new Shop("Muenchen");
		Vehicle vehicleItem = new Vehicle(shop, Vehicle);
		vehicleItem.setLocation(new Shop("Stuttgart"));
		manager.persist(vehicleItem);
		manager.persist(new User(new Person("Hans", "Mustermann")));
		manager.persist(new User(new Person("Franz", "Mueller")));
		manager.persist(new User(new Person("Herbert", "Schmitt")));
		manager.persist(new User(new Person("Ingo", "Meyer")));
		manager.persist(new User(new Person("Mathias", "Mayer")));
		manager.persist(new User(new Person("Michael", "Anst�dt")));
		manager.persist(new User(new Person("Ralf", "Gross")));
		manager.flush();
		manager.clear();
	}



	@Test public void testPagingBySelectNutzer() {

		Query createQuery = manager.createQuery("SELECT n FROM User n");
		createQuery.setMaxResults(3);
		boolean fertig = false;
		while(!fertig){
			int nextFirstResultIndex = incrementFirstResultWithResultCount(createQuery);
			createQuery.setFirstResult(nextFirstResultIndex);
			List resultList = createQuery.getResultList();
			fertig = resultList.isEmpty();
		}
		assertEquals(2,createQuery.setFirstResult(5).setMaxResults(3)
				.getResultList().size());
	}



	private int incrementFirstResultWithResultCount(Query createQuery) {
		return createQuery.getFirstResult() + createQuery.getMaxResults();
	}
}
