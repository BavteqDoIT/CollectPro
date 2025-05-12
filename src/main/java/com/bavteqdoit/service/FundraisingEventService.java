package com.bavteqdoit.service;

import com.bavteqdoit.entity.FundraisingEvent;
import com.bavteqdoit.repository.FundraisingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundraisingEventService {
    private final FundraisingEventRepository fundraisingEventRepository;

    public FundraisingEvent createFundraisingEvent(FundraisingEvent fe) {
        return fundraisingEventRepository.save(fe);
    }

    public List<FundraisingEvent> findAll() {
        return fundraisingEventRepository.findAll();
    }

    public FundraisingEvent findById(Long id) {
        return fundraisingEventRepository.findById(id).orElse(null);
    }
}
