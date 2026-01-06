package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.rentacar.model.VehicleType;
import net.rentacar.model.Nutzer;
import net.rentacar.model.Person;
import net.rentacar.model.PersonKey;

public class TestConnection extends AbstractJPATestCase {

	private Long id;
	private Long nutzerId;

	@Override
	public void setUp() throws Exception {
		
		VehicleType Vehicle = new VehicleType("VW", "Golf", 120, 200);
		manager.persist(Vehicle);
		
		Person p = new Person();
		p.setFirstname("A");
		p.setLastname("B");
		
		manager.persist(p);
		
		this.id = Vehicle.getId();
		Nutzer n = new Nutzer("a", "b");
		manager.persist(n);
		this.nutzerId = n.getId();
		manager.flush();
		manager.clear();
	}

	@Test public void testFind() {
		manager.find(Person.class, new PersonKey("A","B"));
		assertEquals(id, super.manager.find(VehicleType.class, id).getId());
		assertEquals(nutzerId, super.manager.find(Nutzer.class, nutzerId)
				.getId());
	}

}
