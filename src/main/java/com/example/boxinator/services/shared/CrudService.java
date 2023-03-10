package com.example.boxinator.services.shared;

import java.util.List;

public interface CrudService<T, U> {

    T create(U dto);

    T getById(Long id);

    List<T> getAll();

    Long deleteById(Long id);

    T update(Long id, U dto);
}
