package net.rentacar.model.listener;

import jakarta.persistence.PostPersist;

import net.rentacar.model.VehicleType;

public class VehicleTypeListener {

	public static boolean gewonnen = false;
	
	@PostPersist
	public void doAfterInsert(Object inserted)
	{
		if (inserted instanceof VehicleType) {
			VehicleType Vehicle = (VehicleType) inserted;
			gewonnen = !Vehicle.isCalled;
		}
	}

}
