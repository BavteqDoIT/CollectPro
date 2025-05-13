package com.bavteqdoit.service;

import com.bavteqdoit.entity.Box;
import com.bavteqdoit.repository.BoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoxService {
    private final BoxRepository boxRepository;

    public Box createBox(Box box) {
        return boxRepository.save(box);
    }

    public List<Box> findAllBoxes() {
        return boxRepository.findAll();
    }

    public Box findBoxById(Long id) {
        return boxRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box not found!"));
    }

    public Box updateBox(long id, Box updatedBox) {
        Box existingBox = boxRepository.findById(id).orElseThrow(() -> new RuntimeException("Box not found"));

        existingBox.setPrice(updatedBox.getPrice());
        existingBox.setRented(updatedBox.isRented());
        existingBox.setStartDate(updatedBox.getStartDate());
        existingBox.setEndDate(updatedBox.getEndDate());
        existingBox.setSum(updatedBox.getSum());

        return boxRepository.save(existingBox);
    }




}
