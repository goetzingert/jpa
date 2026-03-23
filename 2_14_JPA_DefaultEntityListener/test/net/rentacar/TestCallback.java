package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import net.rentacar.model.Vehicle;
import net.rentacar.model.VehicleType;
import net.rentacar.model.Shop;
import net.rentacar.model.Truck;
import net.rentacar.model.Model;
import net.rentacar.model.listener.DefaultListener;
import net.rentacar.model.listener.VehicleListener;

import org.junit.jupiter.api.Test;

public class TestCallback extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		
	}

	@Test
	public void testCallbackFailed() {
		VehicleType Vehicle = new Truck("1", new Model("VW", "Golf"), 120, 200,
				10000);
		manager.persist(Vehicle);
		assertFalse(Vehicle.isCalled);
	}

	@Test
	public void testCallback() {
		VehicleType vehicleType = new Truck("1", new Model("VW", "Golf"), 120, 200,
				10000);
		manager.persist(vehicleType);
		Shop Shop = new Shop("1", "Muenchen");
		Vehicle vehicle = new Vehicle("1", Shop, vehicleType);
		vehicle.setLocation(new Shop("2", "Stuttgart"));
		manager.persist(vehicle);
		manager.flush();
		assertTrue(vehicleType.isCalled);
		assertTrue(VehicleListener.gewonnen);
		assertTrue(new java.io.File(DefaultListener.VERZEICHNIS
				+ File.separatorChar + "Shop1." + DefaultListener.DATEIENDUNG)
				.exists());
	}
}
