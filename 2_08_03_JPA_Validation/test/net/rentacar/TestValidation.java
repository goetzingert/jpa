package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.validation.ConstraintViolationException;
import net.rentacar.model.ValidatedVehicle;
import org.junit.jupiter.api.Test;

public class TestValidation extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
	}

	@Test
	public void validVehiclePersists() {
		ValidatedVehicle vehicle = new ValidatedVehicle("VW", 120);
		assertDoesNotThrow(() -> {
			manager.persist(vehicle);
			manager.flush();
		});
	}

	@Test
	public void invalidVehicleTriggersValidation() {
		ValidatedVehicle vehicle = new ValidatedVehicle("", 0);
		assertThrows(ConstraintViolationException.class, () -> {
			manager.persist(vehicle);
			manager.flush();
		});
	}
}
