package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import net.rentacar.model.VehicleType;
import net.rentacar.model.VehicleItem;
import net.rentacar.model.Shop;
import net.rentacar.model.Truck;
import net.rentacar.model.Model;
import net.rentacar.model.listener.DefaultListener;
import net.rentacar.model.listener.VehicleTypeListener;

import org.junit.jupiter.api.Test;

public class TestCallback extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		
	}

	@Test public void testCallbackFailed() {
		VehicleType Vehicle = new Truck("1", new Model("VW", "Golf"), 120, 200,
				10000);
		manager.persist(Vehicle);
		assertFalse(Vehicle.isCalled);
	}

	@Test public void testCallback() {
		VehicleType Vehicle = new Truck("1", new Model("VW", "Golf"), 120, 200,
				10000);
		manager.persist(Vehicle);
		Shop Shop = new Shop("1", "Muenchen");
		VehicleItem VehicleItem = new VehicleItem("1", Shop, Vehicle);
		VehicleItem.setLocation(new Shop("2", "Stuttgart"));
		manager.persist(VehicleItem);
		manager.flush();
		assertTrue(Vehicle.isCalled);
		assertTrue(VehicleTypeListener.gewonnen);
		assertTrue(new java.io.File(DefaultListener.VERZEICHNIS+File.separatorChar+"Shop1."+DefaultListener.DATEIENDUNG).exists());
	}
}
