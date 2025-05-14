package com.bavteqdoit.service;

import com.bavteqdoit.entity.Box;
import com.bavteqdoit.repository.BoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoxService {
    private final BoxRepository boxRepository;

    public Box createBox(Box box) {
        return boxRepository.save(box);
    }

    public List<Box> getAllBoxes() {
        return boxRepository.findAll();
    }

    public Box getBoxById(Long id) {
        return boxRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box not found!"));
    }

    public Box updateBox(long id, Box updatedBox) {
        Box existingBox = getBoxById(id);

        existingBox.setPrice(updatedBox.getPrice());
        existingBox.setRented(updatedBox.isRented());
        existingBox.setStartDate(updatedBox.getStartDate());
        existingBox.setEndDate(updatedBox.getEndDate());
        existingBox.setSum(updatedBox.getSum());

        return boxRepository.save(existingBox);
    }

    public List<Box> deleteBox(long id) {
        Box existingBox = getBoxById(id);
        if (emptyBox(existingBox)) {
            if (existingBox.isRented()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Deleting rented box is not allowed");
            } else {
                boxRepository.deleteById(id);
                return boxRepository.findAll();
            }
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete not empty box!");
        }
    }

    public boolean emptyBox(Box box) {
        boolean emptyBox = false;
        if ((box.getSum().compareTo(BigDecimal.ZERO) == 0)) {
            emptyBox = true;
        }
        return emptyBox;
    }

    public Box rentBox(long id, int days) {
        Box existingBox = getBoxById(id);
        if(!existingBox.isRented()) {
            existingBox.setRented(true);
            existingBox.setStartDate(LocalDate.now());
            existingBox.setEndDate(LocalDate.now().plusDays(days));
            boxRepository.save(existingBox);
            return existingBox;
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot rent rented box!");
    }
}
