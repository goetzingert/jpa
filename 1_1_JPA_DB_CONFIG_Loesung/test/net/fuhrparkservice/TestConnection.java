package net.fuhrparkservice;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestConnection extends AbstractJPATestCase {

	@Test
	public void testConnection() {
		assertTrue(super.manager.isOpen());
	}

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub

	}

}
