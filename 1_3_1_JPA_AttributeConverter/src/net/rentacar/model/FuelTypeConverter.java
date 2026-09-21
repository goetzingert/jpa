package net.rentacar.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// TODO: @Converter(autoApply = true) annotieren und AttributeConverter<FuelType, String> implementieren
public class FuelTypeConverter implements AttributeConverter<FuelType, String> {

	@Override
	public String convertToDatabaseColumn(FuelType attribute) {
		// TODO: FuelType in einbuchstabigen DB-Code ("P", "D", "E", "H") wandeln
		return null;
	}

	@Override
	public FuelType convertToEntityAttribute(String dbData) {
		// TODO: DB-Code zurueck in FuelType enum wandeln
		return null;
	}
}
