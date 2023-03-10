package com.example.boxinator.services.box;

import com.example.boxinator.models.box.BoxTier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoxService {
    List<BoxTier> getAllBoxTiers();
}
