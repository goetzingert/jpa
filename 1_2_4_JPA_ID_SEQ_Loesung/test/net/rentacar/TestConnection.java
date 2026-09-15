package net.rentacar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.rentacar.model.VehicleType;
import net.rentacar.model.User;
import net.rentacar.model.Person;
import net.rentacar.model.PersonKey;

public class TestConnection extends AbstractJPATestCase {

	private Long id;
	private Long userId;

	@Override
	public void setUp() throws Exception {
		
		VehicleType vehicle = new VehicleType("VW", "Golf", 120, 200);
		manager.persist(vehicle);
		
		Person p = new Person();
		p.setFirstname("A");
		p.setLastname("B");
		
		manager.persist(p);
		
		this.id = vehicle.getId();
		User n = new User("a", "b");
		manager.persist(n);
		this.userId = n.getId();
		manager.flush();
		manager.clear();
	}

	@Test public void testFind() {
		manager.find(Person.class, new PersonKey("A","B"));
		assertEquals(id, super.manager.find(VehicleType.class, id).getId());
		assertEquals(userId, super.manager.find(User.class, userId)
				.getId());
	}

}
