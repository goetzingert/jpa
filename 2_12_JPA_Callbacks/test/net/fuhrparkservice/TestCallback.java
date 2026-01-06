package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.VehicleType;
import net.rentacar.model.Truck;
import net.rentacar.model.Model;

import org.junit.jupiter.api.Test;

public class TestCallback extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		
	}

	@Test public void testCallbackFailed() {
		VehicleType Vehicle = new Truck("1", new Model("VW", "Golf"), 120, 200,10000);
		manager.persist(Vehicle);
		assertFalse(Vehicle.isCalled());
	}

	@Test public void testCallback() {
		VehicleType Vehicle = new Truck("1", new Model("VW", "Golf"), 120, 200,10000);
		manager.persist(Vehicle);
		manager.flush();
		assertTrue(Vehicle.isCalled());
	}
}
