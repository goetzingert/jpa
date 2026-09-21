package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.Test;

import net.rentacar.model.PessimisticVehicle;

public class TestPessimisticLocking extends AbstractJPATestCase {

	private Long vehicleId;

	@Override
	public void setUp() throws Exception {
		PessimisticVehicle vehicle = new PessimisticVehicle("B-RC-2026", false);
		manager.persist(vehicle);
		manager.flush();
		vehicleId = vehicle.getId();
		manager.clear();
	}

	@Test
	public void testFindWithPessimisticWriteLock() {
		// Pessimistischer Lock direkt beim Find anfordern (erzeugt SELECT ... FOR UPDATE)
		PessimisticVehicle vehicle = manager.find(
				PessimisticVehicle.class, vehicleId, LockModeType.PESSIMISTIC_WRITE);
		assertNotNull(vehicle);
		assertEquals(LockModeType.PESSIMISTIC_WRITE, manager.getLockMode(vehicle));

		vehicle.setReserved(true);
		manager.flush();
	}

	@Test
	public void testExplicitLockOnManagedEntity() {
		PessimisticVehicle vehicle = manager.find(PessimisticVehicle.class, vehicleId);
		assertEquals(LockModeType.NONE, manager.getLockMode(vehicle));

		// Nachtraegliche Sperre auf existierender Managed Entity anfordern
		manager.lock(vehicle, LockModeType.PESSIMISTIC_WRITE);
		assertEquals(LockModeType.PESSIMISTIC_WRITE, manager.getLockMode(vehicle));
	}
}
