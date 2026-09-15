package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.rentacar.model.ComparableVehicle;

public class TestEqualsHashCode extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
	}

	@Test
	public void sameIdShouldBeEqualInSet() {
		ComparableVehicle first = new ComparableVehicle(1L, "VW", "Golf");
		ComparableVehicle second = new ComparableVehicle(1L, "VW", "Golf");
		Set<ComparableVehicle> vehicles = new HashSet<>();
		vehicles.add(first);
		vehicles.add(second);
		assertEquals(1, vehicles.size());
	}

	@Test
	public void changedBrandStillKeepsSameIdentityForSameId() {
		ComparableVehicle first = new ComparableVehicle(1L, "VW", "Golf");
		ComparableVehicle second = new ComparableVehicle(1L, "VW", "Golf");
		Set<ComparableVehicle> vehicles = new HashSet<>();
		vehicles.add(first);
		first.setBrand("Mercedes");
		assertEquals(1, vehicles.size());
		assertNotEquals(0, vehicles.contains(second) ? 1 : 0);
	}
}
