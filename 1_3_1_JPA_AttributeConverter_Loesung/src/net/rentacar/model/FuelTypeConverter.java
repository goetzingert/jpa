package net.rentacar.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FuelTypeConverter implements AttributeConverter<FuelType, String> {

	@Override
	public String convertToDatabaseColumn(FuelType attribute) {
		return attribute == null ? null : attribute.getDbCode();
	}

	@Override
	public FuelType convertToEntityAttribute(String dbData) {
		return FuelType.fromDbCode(dbData);
	}
}
