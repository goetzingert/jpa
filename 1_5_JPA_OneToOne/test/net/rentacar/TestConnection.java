package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import net.rentacar.model.Model;
import net.rentacar.model.Person;
import net.rentacar.model.Shop;
import net.rentacar.model.User;
import net.rentacar.model.VehicleType;

import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		manager.persist(new VehicleType("1", new Model("VW", "Golf"), 120, 200));
		manager.persist(new Shop("1", "Muenchen"));
		manager.persist(new User("1", new Person("1", "Hans", "Mustermann")));
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindVehicle() {
		// 1. Laden des Fahrzeugtyps
		assertNotNull(manager.find(VehicleType.class, "1").getId());
	}

	@Test
	public void testFindUserAndNavigateToPerson() {
		// 2. Laden von User und Navigation über 1:1 zu Person
		User user = manager.find(User.class, "1");
		assertNotNull(user);
		// TODO: Prüfen, dass getPerson() nicht null ist und die Attribute "Hans" und "Mustermann" stimmen
		assertNotNull(user.getPerson());
		assertEquals("Hans", user.getPerson().getFirstName());
		assertEquals("Mustermann", user.getPerson().getLastName());
	}

	@Test
	public void testCascadePersistPropagatesToPerson() {
		// 3. CascadeType.PERSIST: Persistieren des Users speichert die neue Person automatisch mit
		User newUser = new User("2", new Person("2", "Erika", "Musterfrau"));
		
		// TODO: manager.persist(newUser) aufrufen (ohne separates persist(person))
		manager.persist(newUser);

		manager.flush();
		manager.clear();

		Person loadedPerson = manager.find(Person.class, "2");
		assertNotNull(loadedPerson, "Person sollte durch Kaskadierung in der DB persistiert worden sein");
		assertEquals("Erika", loadedPerson.getFirstName());
	}

	@Test
	public void testUpdatingPersonThroughManagedUserPropagatesOnFlush() {
		// 4. Dirty Checking über die 1:1-Beziehung
		User user = manager.find(User.class, "1");
		// TODO: FirstName der Person auf "Maximilian" ändern

		manager.flush();
		manager.clear();

		Person reloadedPerson = manager.find(Person.class, "1");
		assertNotNull(reloadedPerson);
		assertEquals("Maximilian", reloadedPerson.getFirstName());
	}

	@Test
	public void testRemovingUserDoesNotRemovePersonWithoutCascadeRemove() {
		// 5. Löschen des Users ohne CascadeType.REMOVE lässt die Person in der DB intakt
		User user = manager.find(User.class, "1");
		manager.remove(user);

		manager.flush();
		manager.clear();

		assertNull(manager.find(User.class, "1"));
		assertNotNull(manager.find(Person.class, "1"), "Person existiert weiterhin eigenständig");
	}

	@Test
	public void testFindNonExistingUserReturnsNull() {
		// 6. Randfall: Nicht vorhandener User
		assertNull(manager.find(User.class, "999"));
	}

}
