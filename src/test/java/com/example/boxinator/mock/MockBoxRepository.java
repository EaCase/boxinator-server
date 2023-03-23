package com.example.boxinator.mock;

import com.example.boxinator.services.fee.FeeServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MockBoxRepository {
    @InjectMocks
    private FeeServiceImpl feeService;
}