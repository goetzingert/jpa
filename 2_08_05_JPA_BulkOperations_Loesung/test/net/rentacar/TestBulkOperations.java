package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.rentacar.model.BulkVehicle;

public class TestBulkOperations extends AbstractJPATestCase {

	private Long v1Id;
	private Long v2Id;

	@Override
	public void setUp() throws Exception {
		BulkVehicle v1 = new BulkVehicle("VW", 100.0, true);
		BulkVehicle v2 = new BulkVehicle("VW", 100.0, true);
		BulkVehicle v3 = new BulkVehicle("BMW", 200.0, true);
		manager.persist(v1);
		manager.persist(v2);
		manager.persist(v3);
		manager.flush();
		v1Id = v1.getId();
		v2Id = v2.getId();
	}

	@Test
	public void bulkUpdateBypassesPersistenceContextUntilClearOrRefresh() {
		BulkVehicle managedV1 = manager.find(BulkVehicle.class, v1Id);
		assertEquals(100.0, managedV1.getDailyRate());

		// Bulk Update ausfuehren
		int updated = manager.createQuery(
				"UPDATE BulkVehicle v SET v.dailyRate = 120.0 WHERE v.brand = :brand")
				.setParameter("brand", "VW")
				.executeUpdate();
		assertEquals(2, updated);

		// WICHTIGE BEOBACHTUNG: Im Persistence Context haelt managedV1 weiterhin den alten Stand 100.0!
		assertEquals(100.0, managedV1.getDailyRate(),
				"Managed Entity im 1st-Level-Cache spiegelt Bulk-Update vor clear/refresh NICHT wider");

		// Loesung: Persistence Context leeren und Entity neu laden
		manager.clear();
		BulkVehicle reloadedV1 = manager.find(BulkVehicle.class, v1Id);
		assertEquals(120.0, reloadedV1.getDailyRate(),
				"Nach manager.clear() wird der aktualisierte Stand aus der DB geladen");
	}

	@Test
	public void bulkDeleteRemovesRecordsDirectlyInDatabase() {
		int deleted = manager.createQuery(
				"DELETE FROM BulkVehicle v WHERE v.brand = :brand")
				.setParameter("brand", "BMW")
				.executeUpdate();
		assertEquals(1, deleted);

		manager.clear();
		Long remaining = manager.createQuery(
				"SELECT COUNT(v) FROM BulkVehicle v", Long.class)
				.getSingleResult();
		assertEquals(2L, remaining);
	}
}
