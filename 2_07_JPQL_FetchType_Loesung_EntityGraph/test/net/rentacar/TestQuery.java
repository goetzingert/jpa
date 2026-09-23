package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import jakarta.persistence.EntityGraph;
import org.junit.jupiter.api.Test;

import net.rentacar.model.Car;
import net.rentacar.model.Model;
import net.rentacar.model.Person;
import net.rentacar.model.Shop;
import net.rentacar.model.Truck;
import net.rentacar.model.User;
import net.rentacar.model.Vehicle;
import net.rentacar.model.VehicleType;

public class TestQuery extends AbstractJPATestCase {

	private Vehicle vehicle;

	@Override
	public void setUp() throws Exception {
		VehicleType vehicle1 = new Car(new Model("VW", "Golf"), 120, 200, 2);
		manager.persist(vehicle1);
		VehicleType vehicle2 = new Truck(new Model("Mercedes", "10to"), 120, 200, 10000);
		manager.persist(vehicle2);
		VehicleType vehicle3 = new Car(new Model("BMW", "323"), 150, 220, 4);
		manager.persist(vehicle3);

		Shop shop = new Shop("Muenchen");
		vehicle = new Vehicle(shop, vehicle1);
		vehicle.setLocation(new Shop("Stuttgart"));

		Vehicle vehicle2Inst = new Vehicle(shop, vehicle2);
		manager.persist(vehicle2Inst);
		manager.persist(new User(new Person("Hans", "Mustermann")));
		manager.persist(new User(new Person("Franz", "Mueller")));

		Shop koeln = new Shop("Koeln");
		manager.persist(koeln);

		manager.flush();
		manager.clear();
	}

	@Test
	public void testForVehicleInShopWithEntityGraph() {
		// Dynamischer Entity Graph: carpool wird gezielt fuer diese Abfrage eager geladen
		EntityGraph<Shop> graph = manager.createEntityGraph(Shop.class);
		graph.addAttributeNodes("carpool");

		List<Shop> shops = manager
				.createQuery("SELECT s FROM Shop s", Shop.class)
				.setHint("jakarta.persistence.fetchgraph", graph)
				.getResultList();

		boolean hasLoadedVehicle = false;
		for (Shop s : shops) {
			for (Vehicle item : s.getCarpool()) {
				assertFalse(item.getType().getModel().getBrand().isEmpty());
				hasLoadedVehicle = true;
			}
		}
		assertTrue(hasLoadedVehicle);
	}
}
