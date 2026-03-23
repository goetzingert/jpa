package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

import jakarta.persistence.TypedQuery;

public class TestQuery extends AbstractJPATestCase {

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

	@Test public void testQueryForVehicleTypeMoreThan130HP() {

		TypedQuery<VehicleType> query = null; //TODO
		List<VehicleType> resultList = query.getResultList();

		assertEquals(1, resultList.size());
		assertEquals("BMW", resultList.get(0).getModel().getBrand());
	}
}
