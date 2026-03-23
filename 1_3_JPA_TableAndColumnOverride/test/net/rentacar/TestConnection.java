package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.VehicleType;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private VehicleType VehicleType;

	@Override
	public void setUp() throws Exception {

		VehicleType = new VehicleType("VW", "Golf", 120, 200);
		manager.persist(VehicleType);
		// TODO Add TestData with EntityManager
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class,
				VehicleType.getId()).getId());
	}

}
