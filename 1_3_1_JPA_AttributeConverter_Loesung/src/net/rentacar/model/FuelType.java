package net.rentacar.model;

public enum FuelType {
	PETROL("P"),
	DIESEL("D"),
	ELECTRIC("E"),
	HYBRID("H");

	private final String dbCode;

	FuelType(String dbCode) {
		this.dbCode = dbCode;
	}

	public String getDbCode() {
		return dbCode;
	}

	public static FuelType fromDbCode(String dbCode) {
		if (dbCode == null) {
			return null;
		}
		for (FuelType type : values()) {
			if (type.dbCode.equalsIgnoreCase(dbCode)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unbekannter DB-Code fuer FuelType: " + dbCode);
	}
}
