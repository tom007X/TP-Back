package com.example.ShippingMicroservice.service.interfaces;

import java.util.List;

public interface BasicService<E,I>{

    E save(E entity);
    E findById(I id);
    List<E> findAll();
    void remove(E entity);
}
