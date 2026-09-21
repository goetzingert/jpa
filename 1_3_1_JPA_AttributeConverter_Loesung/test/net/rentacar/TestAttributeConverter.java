package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import net.rentacar.model.ConvertedVehicle;
import net.rentacar.model.FuelType;

public class TestAttributeConverter extends AbstractJPATestCase {

	private Long vehicleId;

	@Override
	public void setUp() throws Exception {
		ConvertedVehicle vehicle = new ConvertedVehicle("Tesla", "Model 3", FuelType.ELECTRIC);
		manager.persist(vehicle);
		manager.flush();
		vehicleId = vehicle.getId();
		manager.clear();
	}

	@Test
	public void testAttributeConverterMapsEnumToSingleCharacterColumn() {
		ConvertedVehicle loaded = manager.find(ConvertedVehicle.class, vehicleId);
		assertNotNull(loaded);
		assertEquals(FuelType.ELECTRIC, loaded.getFuelType());

		// DB-Zustand per Native Query pruefen: in der Spalte steht 'E', nicht 'ELECTRIC' und nicht '2'
		String rawDbValue = (String) manager.createNativeQuery(
				"SELECT fuelType FROM ConvertedVehicle WHERE id = " + vehicleId)
				.getSingleResult();
		assertEquals("E", rawDbValue);
	}
}
