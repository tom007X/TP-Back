package com.example.ShippingMicroservice.utils;

import com.example.ShippingMicroservice.model.ContainerStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ContainerStatusCodeConverter implements AttributeConverter<ContainerStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ContainerStatus attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public ContainerStatus convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : ContainerStatus.fromCode(dbData);
    }
}
