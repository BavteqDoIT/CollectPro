package com.bavteqdoit.service;

import com.bavteqdoit.entity.Box;
import com.bavteqdoit.repository.BoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoxService {
    private final BoxRepository boxRepository;

    public Box create(Box box) {
        return boxRepository.save(box);
    }

    public List<Box> getAll() {
        return boxRepository.findAll();
    }

    public Box getById(Long id) {
        return boxRepository.findById(id).orElse(null);
    }
}
