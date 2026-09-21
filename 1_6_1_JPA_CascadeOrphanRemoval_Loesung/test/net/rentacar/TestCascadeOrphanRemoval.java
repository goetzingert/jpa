package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import net.rentacar.model.DamageRecord;
import net.rentacar.model.RentalContract;

public class TestCascadeOrphanRemoval extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
	}

	@Test
	public void testCascadePersistPropagatesToChildren() {
		RentalContract contract = new RentalContract("Anna Schmidt");
		DamageRecord d1 = new DamageRecord("Kratzer Stossstange");
		DamageRecord d2 = new DamageRecord("Steinschlag Frontscheibe");
		contract.addDamageRecord(d1);
		contract.addDamageRecord(d2);

		// Nur Parent persistieren - Children muessen durch CascadeType.PERSIST automatisch gespeichert werden
		manager.persist(contract);
		manager.flush();
		Long contractId = contract.getId();
		Long d1Id = d1.getId();
		Long d2Id = d2.getId();
		assertNotNull(contractId);
		assertNotNull(d1Id);
		assertNotNull(d2Id);

		manager.clear();

		RentalContract loaded = manager.find(RentalContract.class, contractId);
		assertEquals(2, loaded.getDamageRecords().size());
	}

	@Test
	public void testOrphanRemovalDeletesChildWhenRemovedFromCollection() {
		RentalContract contract = new RentalContract("Bernd Mueller");
		DamageRecord d1 = new DamageRecord("Delle Tuer");
		contract.addDamageRecord(d1);
		manager.persist(contract);
		manager.flush();
		Long d1Id = d1.getId();
		manager.clear();

		// Child aus der Collection des Parents entfernen
		RentalContract loaded = manager.find(RentalContract.class, contract.getId());
		DamageRecord toRemove = loaded.getDamageRecords().get(0);
		loaded.removeDamageRecord(toRemove);
		manager.flush();
		manager.clear();

		// Durch orphanRemoval = true muss der Datensatz physisch aus der DB geloescht sein
		DamageRecord orphan = manager.find(DamageRecord.class, d1Id);
		assertNull(orphan, "Verwaister DamageRecord haette durch orphanRemoval geloescht werden muessen");
	}

	@Test
	public void testCascadeRemoveDeletesAllChildren() {
		RentalContract contract = new RentalContract("Clara Weber");
		DamageRecord d1 = new DamageRecord("Felge zerkratzt");
		contract.addDamageRecord(d1);
		manager.persist(contract);
		manager.flush();
		Long contractId = contract.getId();
		Long d1Id = d1.getId();
		manager.clear();

		// Parent loeschen -> Children werden via CascadeType.REMOVE mitgeloescht
		RentalContract loaded = manager.find(RentalContract.class, contractId);
		manager.remove(loaded);
		manager.flush();
		manager.clear();

		assertNull(manager.find(RentalContract.class, contractId));
		assertNull(manager.find(DamageRecord.class, d1Id));
	}
}
