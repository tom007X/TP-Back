package com.example.ShippingMicroservice.utils;
import com.example.ShippingMicroservice.model.SectionStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SectionStatusCodeConverter implements AttributeConverter<SectionStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(SectionStatus attribute) {
        return attribute == null ? null : attribute.getCode();
    }
    @Override
    public SectionStatus convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : SectionStatus.fromCode(dbData);
    }
}