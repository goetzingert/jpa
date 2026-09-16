package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.rentacar.model.VehicleType;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		manager.persist(new VehicleType("1", "VW", "Golf", 120, 200));
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFind() {
		// 1. Entity per Primärschlüssel aus der Datenbank laden
		VehicleType vehicleType = manager.find(VehicleType.class, "1");
		assertNotNull(vehicleType);
		assertEquals("1", vehicleType.getId());
		assertEquals("VW", vehicleType.getBrand());
		assertEquals("Golf", vehicleType.getModell());
		assertEquals(120, vehicleType.getHp());
	}

	@Test
	public void testDirtyCheckingTriggersAutomaticUpdate() {
		// 2. Dirty Checking: Änderung an gemanagter Entity erfordert kein manuelles update()
		VehicleType vehicleType = manager.find(VehicleType.class, "1");
		vehicleType.setHp(150);

		// Synchronisation mit der Datenbank
		manager.flush();
		manager.clear();

		VehicleType reloaded = manager.find(VehicleType.class, "1");
		assertEquals(150, reloaded.getHp());
	}

	@Test
	public void testContainsReflectsPersistenceContextState() {
		// 3. Zustand im Persistence Context prüfen (managed vs. detached)
		VehicleType vehicleType = manager.find(VehicleType.class, "1");
		assertTrue(manager.contains(vehicleType), "Entity sollte im Persistence Context gemanagt sein");

		manager.detach(vehicleType);
		assertFalse(manager.contains(vehicleType), "Nach detach() ist die Entity unmanaged");

		manager.clear();
		assertFalse(manager.contains(vehicleType), "Nach clear() sind alle Entities detached");
	}

	@Test
	public void testDetachAndMergeLifecycle() {
		// 4. Detach und Merge: Änderungen an detached Entity wieder in Context übernehmen
		VehicleType vehicleType = manager.find(VehicleType.class, "1");
		manager.detach(vehicleType);

		vehicleType.setHp(180);

		// merge gibt eine neue, gemanagte Kopie zurück
		VehicleType merged = manager.merge(vehicleType);
		assertTrue(manager.contains(merged), "Merged-Instanz ist gemanagt");
		assertFalse(manager.contains(vehicleType), "Ursprüngliche detached Instanz bleibt unmanaged");

		manager.flush();
		manager.clear();

		VehicleType reloaded = manager.find(VehicleType.class, "1");
		assertEquals(180, reloaded.getHp());
	}

	@Test
	public void testRemoveDeletesEntityFromDatabase() {
		// 5. Entity aus der Datenbank löschen (remove)
		VehicleType vehicleType = manager.find(VehicleType.class, "1");
		manager.remove(vehicleType);

		manager.flush();
		manager.clear();

		VehicleType deleted = manager.find(VehicleType.class, "1");
		assertNull(deleted, "Nach remove() und flush() sollte die Entity in der DB nicht mehr existieren");
	}

	@Test
	public void testFindNonExistingIdReturnsNull() {
		// 6. Randfall: Nicht vorhandener Primärschlüssel liefert null (keine Exception)
		VehicleType nonExisting = manager.find(VehicleType.class, "non-existing-id");
		assertNull(nonExisting);
	}

}
