package com.example.boxinator.services.box;

import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.box.BoxTier;
import com.example.boxinator.repositories.box.BoxRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoxServiceImpl implements BoxService {
    private final BoxRepository boxRepository;

    public BoxServiceImpl(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }

    @Override
    public List<BoxTier> getAllBoxTiers() {
        return boxRepository.findAll();
    }

    @Override
    public BoxTier getById(Long id) {
        return boxRepository.findById(id).orElseThrow(() -> new ApplicationException(
                "Box tier with the id: " + id + " does not exist.",
                HttpStatus.NOT_FOUND
        ));
    }
}
