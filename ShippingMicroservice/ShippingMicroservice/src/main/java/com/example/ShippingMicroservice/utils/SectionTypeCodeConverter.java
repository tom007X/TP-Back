package com.example.ShippingMicroservice.utils;

import com.example.ShippingMicroservice.model.SectionType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SectionTypeCodeConverter implements AttributeConverter<SectionType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(SectionType attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public SectionType convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : SectionType.fromCode(dbData);
    }

}
