package com.example.ShippingMicroservice.utils;

import com.example.ShippingMicroservice.model.ShippingRequestStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ShippingRequestStatusCodeConverter
        implements AttributeConverter<ShippingRequestStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ShippingRequestStatus attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public ShippingRequestStatus convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : ShippingRequestStatus.fromCode(dbData);
    }
}