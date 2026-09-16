package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.TypedQuery;

import net.rentacar.model.*;

import org.junit.jupiter.api.Test;

public class TestQuery extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {

		VehicleType Vehicle = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(Vehicle);
		VehicleType Vehicle2 = new Truck(new Model("Mercedes", "10to"), 120,
				200, 10000);
		manager.persist(Vehicle2);
		VehicleType Vehicle3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(Vehicle3);
		Shop shop = new Shop("Muenchen");
		Vehicle vehicle = new Vehicle(shop, Vehicle);
		vehicle.setLocation(new Shop("Stuttgart"));
		manager.persist(vehicle);
		manager.persist(new User(new Person("Hans", "Mustermann")));
		manager.persist(new User(new Person("Franz", "Mueller")));
		manager.persist(new User(new Person("Herbert", "Schmitt")));
		manager.persist(new User(new Person("Ingo", "Meyer")));
		manager.persist(new User(new Person("Mathias", "Mayer")));
		manager.persist(new User(new Person("Michael", "Anstaedt")));
		manager.persist(new User(new Person("Ralf", "Gross")));
		manager.flush();
		manager.clear();
	}

	@Test
	public void testPagingIteratesThroughAllPagesWithFixedPageSize() {
		// 1. Paging in Schleife mit setMaxResults(3) und schrittweiser Erhöhung von setFirstResult()
		TypedQuery<User> query = null; // TODO: Query "SELECT n FROM User n ORDER BY n.id" mit setMaxResults(3)
		int pageSize = 3;

		List<User> allPagedUsers = new ArrayList<>();
		int offset = 0;
		List<User> page;

		// TODO: Schleife implementieren, die solange Seiten lädt, bis page.isEmpty() ist

		assertNotNull(query);
		assertEquals(7, allPagedUsers.size());
	}

	@Test
	public void testDeterministicPagingRequiresOrderBy() {
		// 2. Deterministisches Paging: Sortiere nach Nachname aufsteigend
		TypedQuery<User> query = null; // TODO: "SELECT n FROM User n ORDER BY n.person.lastName ASC, n.person.firstName ASC"

		int pageSize = 3;

		// Seite 1 (Offset 0, Limit 3): Anstaedt, Gross, Mayer
		List<User> page1 = query != null ? query.setFirstResult(0).setMaxResults(pageSize).getResultList() : null;
		assertNotNull(page1);
		assertEquals(3, page1.size());
		assertEquals("Anstaedt", page1.get(0).getPerson().getLastName());
		assertEquals("Gross", page1.get(1).getPerson().getLastName());
		assertEquals("Mayer", page1.get(2).getPerson().getLastName());

		// Seite 2 (Offset 3, Limit 3): Meyer, Mueller, Mustermann
		List<User> page2 = query.setFirstResult(3).setMaxResults(pageSize).getResultList();
		assertEquals(3, page2.size());
		assertEquals("Meyer", page2.get(0).getPerson().getLastName());
		assertEquals("Mueller", page2.get(1).getPerson().getLastName());
		assertEquals("Mustermann", page2.get(2).getPerson().getLastName());

		// Seite 3 (Offset 6, Limit 3): Schmitt (Restseite)
		List<User> page3 = query.setFirstResult(6).setMaxResults(pageSize).getResultList();
		assertEquals(1, page3.size());
		assertEquals("Schmitt", page3.get(0).getPerson().getLastName());
	}

	@Test
	public void testCountTotalElementsAndCalculatePageCount() {
		// 3. Gesamtanzahl der Datensätze per COUNT(n) ermitteln und Seitenanzahl berechnen
		Long totalCount = null; // TODO: Query "SELECT COUNT(n) FROM User n" mit getSingleResult()

		assertNotNull(totalCount);
		assertEquals(7L, totalCount);

		int pageSize = 3;
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);
		assertEquals(3, totalPages);
	}

	@Test
	public void testPagingBeyondTotalElementsReturnsEmptyList() {
		// 4. Randfall: setFirstResult(100) liefert eine leere Liste (keine Exception)
		TypedQuery<User> query = null; // TODO: Query mit setFirstResult(100).setMaxResults(3)
		List<User> result = query != null ? query.getResultList() : null;

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testPagingWithZeroMaxResultsReturnsEmptyList() {
		// 5. Randfall: setMaxResults(0) liefert 0 Treffer
		TypedQuery<User> query = null; // TODO: Query mit setFirstResult(0).setMaxResults(0)
		List<User> result = query != null ? query.getResultList() : null;

		assertNotNull(result);
		assertEquals(0, result.size());
	}
}
