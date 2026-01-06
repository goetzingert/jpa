package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;
import net.rentacar.model.Kunde;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private Long nutzerId;

	@Override
	public void setUp() throws Exception {
		
		Kunde n = new Kunde("a", "b");
		manager.persist(n);
		manager.flush();
		manager.clear();
	}

	@Test public void testFind() {
		assertNotNull(super.manager.find(Kunde.class, null));
	}

}
