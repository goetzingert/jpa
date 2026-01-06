select 	Vehiclety0_.id as id0_, 
		Vehiclety0_.maxKph as maxKph0_, 
		Vehiclety0_.brand as brand0_, 
		Vehiclety0_.modell as modell0_, 
		Vehiclety0_.HP as HP0_, 
		Vehiclety0_1_.maxLoad as maxZulad1_5_, 
		Vehiclety0_2_.doors as doors6_, 
		case when Vehiclety0_1_.id is not null then 1 when Vehiclety0_2_.id is not null then 2 when Vehiclety0_.id is not null then 0 end as clazz_ 
		from tbl_VehicleType Vehiclety0_
		left outer join TBL_TRUCK Vehiclety0_1_ on Vehiclety0_.id=Vehiclety0_1_.id
		left outer join TBL_Car Vehiclety0_2_ on Vehiclety0_.id=Vehiclety0_2_.id
