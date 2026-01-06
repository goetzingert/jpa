package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.VehicleType;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private Long vehicleTypeId;

	@Override
	public void setUp() throws Exception {
		
		VehicleType vw_golf = new VehicleType("VW", "Golf", 120, 200);
		manager.persist(vw_golf);
		this.vehicleTypeId = vw_golf.getId();

		manager.flush();
		manager.clear();
	}

	@Test
	public void testFind() {
		assertEquals(vehicleTypeId, super.manager.find(VehicleType.class, vehicleTypeId).getId());
	}

}
