package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;

import net.rentacar.model.LockableVehicle;

import org.junit.jupiter.api.Test;

public class TestOptimisticLocking extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		LockableVehicle vehicle = new LockableVehicle("VW");
		manager.persist(vehicle);
		manager.getTransaction().commit();
	}

	@Test
	public void optimisticLockingRejectsStaleVersions() {
		EntityManager firstManager = managerFactory.createEntityManager();
		EntityManager secondManager = managerFactory.createEntityManager();
		firstManager.getTransaction().begin();
		secondManager.getTransaction().begin();

		LockableVehicle first = firstManager.find(LockableVehicle.class, 1L);
		LockableVehicle second = secondManager.find(LockableVehicle.class, 1L);

		first.setBrand("Mercedes");
		firstManager.getTransaction().commit();

		second.setBrand("BMW");
		assertThrows(OptimisticLockException.class, () -> {
			secondManager.flush();
		});
		secondManager.getTransaction().rollback();

		EntityManager checkManager = managerFactory.createEntityManager();
		assertEquals("Mercedes", checkManager.find(LockableVehicle.class, 1L).getBrand());
		checkManager.close();
		firstManager.close();
		secondManager.close();
	}
}
