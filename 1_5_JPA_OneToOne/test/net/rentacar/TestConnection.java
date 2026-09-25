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

	private VehicleType vehicleType;
	private Shop shop;
	private User user;

	@Override
	public void setUp() throws Exception {
		vehicleType = new VehicleType(new Model("VW", "Golf"), 120, 200);
		manager.persist(vehicleType);
		shop = new Shop("Muenchen");
		manager.persist(shop);
		user = new User(new Person("Hans", "Mustermann"));
		manager.persist(user);
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindVehicle() {
		// 1. Laden des Fahrzeugtyps
		assertNotNull(manager.find(VehicleType.class, vehicleType.getId()).getId());
	}

	@Test
	public void testFindUserAndNavigateToPerson() {
		// 2. Laden von User und Navigation über 1:1 zu Person
		User loadedUser = manager.find(User.class, user.getId());
		assertNotNull(loadedUser);
		// TODO: Prüfen, dass getPerson() nicht null ist und die Attribute "Hans" und "Mustermann" stimmen
		assertNotNull(loadedUser.getPerson());
		assertEquals("Hans", loadedUser.getPerson().getFirstName());
		assertEquals("Mustermann", loadedUser.getPerson().getLastName());
	}

	@Test
	public void testCascadePersistPropagatesToPerson() {
		// 3. CascadeType.PERSIST: Persistieren des Users speichert die neue Person automatisch mit
		Person newPerson = new Person("Erika", "Musterfrau");
		User newUser = new User(newPerson);
		
		// TODO: manager.persist(newUser) aufrufen (ohne separates persist(person))
		manager.persist(newUser);

		manager.flush();
		manager.clear();

		Person loadedPerson = manager.find(Person.class, newPerson.getId());
		assertNotNull(loadedPerson, "Person sollte durch Kaskadierung in der DB persistiert worden sein");
		assertEquals("Erika", loadedPerson.getFirstName());
	}

	@Test
	public void testUpdatingPersonThroughManagedUserPropagatesOnFlush() {
		// 4. Dirty Checking über die 1:1-Beziehung
		User loadedUser = manager.find(User.class, user.getId());
		// TODO: FirstName der Person auf "Maximilian" ändern

		manager.flush();
		manager.clear();

		Person reloadedPerson = manager.find(Person.class, user.getPerson().getId());
		assertNotNull(reloadedPerson);
		assertEquals("Maximilian", reloadedPerson.getFirstName());
	}

	@Test
	public void testRemovingUserDoesNotRemovePersonWithoutCascadeRemove() {
		// 5. Löschen des Users ohne CascadeType.REMOVE lässt die Person in der DB intakt
		long personId = user.getPerson().getId();
		long userId = user.getId();
		User loadedUser = manager.find(User.class, userId);
		manager.remove(loadedUser);

		manager.flush();
		manager.clear();

		assertNull(manager.find(User.class, userId));
		assertNotNull(manager.find(Person.class, personId), "Person existiert weiterhin eigenständig");
	}

	@Test
	public void testFindNonExistingUserReturnsNull() {
		// 6. Randfall: Nicht vorhandener User
		assertNull(manager.find(User.class, 999L));
	}

}
