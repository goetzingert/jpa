package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.VehicleType;
import net.rentacar.model.Nutzer;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private Long id;
	private Long nutzerId;

	@Override
	public void setUp() throws Exception {
		
		VehicleType Vehicle = new VehicleType("VW", "Golf", 120, 200);
		manager.persist(Vehicle);
		this.id = Vehicle.getId();
		Nutzer n = new Nutzer("a", "b");
		manager.persist(n);
		this.nutzerId = n.getId();
		manager.flush();
		manager.clear();
	}

	@Test public void testFind() {
		assertEquals(id, super.manager.find(VehicleType.class, id).getId());
		assertEquals(nutzerId, super.manager.find(Nutzer.class, nutzerId)
				.getId());
	}

}
