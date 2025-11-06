package com.example.ShippingMicroservice.service;

import java.util.List;

public interface ServiceInterface<E,I>{

    E save(E entity);
    E findById(I id);
    List<E> findAll();
    void remove(E entity);
}
