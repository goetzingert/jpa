package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.rentacar.model.Car;
import net.rentacar.model.Model;
import net.rentacar.model.VehicleType;
import org.junit.jupiter.api.Test;

public class TestLifecycle extends AbstractJPATestCase {
	@Override
	protected void setUp() {
	}

	@Test
	public void persistFlushClearAndFindShowEntityLifecycle() {
		VehicleType vehicleType = new Car(new Model("VW", "Golf"), 120, 200, 2);
		assertFalse(manager.contains(vehicleType));
		manager.persist(vehicleType);
		assertTrue(manager.contains(vehicleType));
		manager.flush();
		assertNotNull(vehicleType.getId());
		manager.clear();
		assertFalse(manager.contains(vehicleType));
		VehicleType reloaded = manager.find(VehicleType.class, vehicleType.getId());
		assertNotNull(reloaded);
		assertEquals("VW", reloaded.getModel().getBrand());
		assertEquals("Golf", reloaded.getModel().getModell());
	}
}
