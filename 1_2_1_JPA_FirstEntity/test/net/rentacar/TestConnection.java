package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.VehicleType;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private long id;

	@Override
	public void setUp() throws Exception {
		VehicleType vehicleType = new VehicleType( "VW", "Golf", 120, 200);
		manager.persist(vehicleType);
		id = vehicleType.getId();
	}

	@Test
	public void testFind() {
		//SELECT * from VehicleType where id= ...
		VehicleType vehicleType = super.manager.find(VehicleType.class, id);
		assertNotNull(vehicleType);
		assertEquals(id, vehicleType.getId());
	}

}
