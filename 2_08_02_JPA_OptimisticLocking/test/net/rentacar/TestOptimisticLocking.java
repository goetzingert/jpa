package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;

import org.junit.jupiter.api.Test;

import net.rentacar.model.LockableVehicle;

public class TestOptimisticLocking extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		LockableVehicle vehicle = new LockableVehicle("VW");
		manager.persist(vehicle);
		manager.getTransaction().commit();
	}

	@Test
	public void optimisticLockingRejectsStaleVersions() {
		EntityManager otherManager = managerFactory.createEntityManager();
		otherManager.getTransaction().begin();

		LockableVehicle first = manager.find(LockableVehicle.class, 1L);
		LockableVehicle second = otherManager.find(LockableVehicle.class, 1L);

		manager.getTransaction().begin();
		first.setBrand("Mercedes");
		manager.flush();
		manager.getTransaction().commit();

		second.setBrand("BMW");
		assertThrows(OptimisticLockException.class, () -> {
			otherManager.getTransaction().commit();
		});
		otherManager.close();
	}
}
