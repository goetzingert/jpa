package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.VehicleType;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private long id;

	@Override
	public void setUp() throws Exception {
		VehicleType fahrezug = new VehicleType( "VW", "Golf", 120, 200);
		manager.persist(fahrezug);
		id = fahrezug.getId();
	}

	@Test
	public void testFind() {
		//SELECT * from VehicleType where id= ...
		VehicleType VehicleType = super.manager.find(VehicleType.class, id);
		assertNotNull(VehicleType);
		assertEquals(id, VehicleType.getId());
	}

}
