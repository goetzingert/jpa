package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;

import net.rentacar.model.*;
import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {

	private VehicleType VehicleType;
	private Shop Shop;
	private Nutzer nutzer;

	@Override
	public void setUp() throws Exception {
		
		VehicleType = new VehicleType(new Model("VW", "Golf"), 120, 200);
		manager.persist(VehicleType);
		Shop = new Shop("Muenchen");
		manager.persist(Shop);
		nutzer = new Nutzer( "Hans", "Mustermann");
		manager.persist(nutzer);
		//manager.persist(nutzer.getPerson());
		manager.flush();
		manager.clear();
	}

	@Test public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class, VehicleType.getId()).getId());
	}

	@Test public void testFindNutzer() {
		// TODO find Nutzer with EntityManager
		assertNotNull(super.manager.find(Nutzer.class, nutzer.getId()).getId());
	}

	@Test public void testFindShop() {
		// TODO find Shop with EntityManager
		assertNotNull(super.manager.find(Shop.class, Shop.getId()).getId());
	}

	@Test public void testFindModel() {
		assertNotNull(super.manager.find(VehicleType.class, VehicleType.getId()).getModel()
				.getBrand());
	}
	
	@Test public void testFindPersonByNutzer() {
		Nutzer nutzer = super.manager.find(Nutzer.class, this.nutzer.getId());
		Person person = nutzer.getPerson();
		assertNotNull(person
				.getFirstName());
	}

}
