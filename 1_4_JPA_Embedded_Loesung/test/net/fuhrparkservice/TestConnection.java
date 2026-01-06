package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;

import net.rentacar.model.*;
import org.junit.jupiter.api.Test;

import jakarta.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

public class TestConnection extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		
		manager.persist(new VehicleType("1", new VehicleModel("4",new Model("VW", "Golf")), 120, 200));
		manager.persist(new Shop("1", "Muenchen"));
		manager.persist(new Nutzer("1", "Hans", "Mustermann"));
		manager.flush();
		manager.clear();
	}

	@Test public void testFindVehicle() {
		assertNotNull(super.manager.find(VehicleType.class, "1").getId());
	}

	@Test public void testFindNutzer() {
		// TODO find Nutzer with EntityManager
		assertNotNull(super.manager.find(Nutzer.class, "1").getId());
	}

	@Test public void testFindShop() {
		// TODO find Shop with EntityManager
		assertNotNull(super.manager.find(Shop.class, "1").getId());
	}

	@Test public void testFindModel() {
		assertNotNull(super.manager.find(VehicleType.class, "1").getModel()
				.getModel().getBrand());
	}
	@Test public void testFarm() {
		Bauer heinz = new Bauer();
		heinz.setName("Heinz");
		manager.persist(
				heinz);
		Schwein peppaWutz = new Schwein();
		peppaWutz.setGewicht(50);
		peppaWutz.setName("Peppa Wutz");
		//Beziehung setzen
		peppaWutz.setBesitzer(heinz);
		manager.persist(peppaWutz);



		Schwein schorsch = new Schwein();
		schorsch.setGewicht(50);
		schorsch.setName("Schorsch");
		//Beziehung setzen
		schorsch.setBesitzer(heinz);
		manager.persist(schorsch);
		Stall hintermHaus = new Stall();
		hintermHaus.getBewohner().add(peppaWutz);
		hintermHaus.getBewohner().add(schorsch);

		List<String> namenVonSchweinen = Arrays.asList("Papa Wutz", "Schorch");

		manager.createQuery("SELECT s FROM Stall s where s.bewohner IS EMPTY");

		TypedQuery<Schwein> query = manager.createQuery("SELECT s FROM Schwein s where s.name IN :namen", Schwein.class);
		query.setParameter("namen", namenVonSchweinen);



	}

}
