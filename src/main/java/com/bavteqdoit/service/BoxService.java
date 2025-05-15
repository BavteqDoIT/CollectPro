package com.bavteqdoit.service;

import com.bavteqdoit.entity.Box;
import com.bavteqdoit.entity.FundraisingEvent;
import com.bavteqdoit.repository.BoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoxService {
    private final BoxRepository boxRepository;
    private final FundraisingEventService fundraisingEventService;

    public Box createBox(Box box) {
        Objects.requireNonNull(box, "Box is required");
        return boxRepository.save(box);
    }

    public List<Box> getAllBoxes() {
        return boxRepository.findAll();
    }

    public Box getBoxById(Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }
        return boxRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Box not found!"));
    }

    public Box updateBox(Long id, Box updatedBox) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }
        Objects.requireNonNull(updatedBox, "Box is required");
        Box existingBox = getBoxById(id);

        existingBox.setPrice(updatedBox.getPrice());
        existingBox.setRented(updatedBox.isRented());
        existingBox.setStartDate(updatedBox.getStartDate());
        existingBox.setEndDate(updatedBox.getEndDate());
        existingBox.setSum(updatedBox.getSum());

        return boxRepository.save(existingBox);
    }

    public void deleteBox(Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }
        Box existingBox = getBoxById(id);
        if (emptyBox(existingBox)) {
            if (existingBox.isRented()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Deleting rented box is not allowed");
            } else {
                boxRepository.deleteById(id);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete not empty box!");
        }
    }

    public boolean emptyBox(Box box) {
        Objects.requireNonNull(box, "Box is required");
        return box.getSum().compareTo(BigDecimal.ZERO) == 0;
    }

    public Box rentBox(Long id, int days, Long fundraisingEventId) {
        if (id <= 0 || fundraisingEventId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }
        if (days <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of days must be greater than 0");
        }
        Box existingBox = getBoxById(id);
        FundraisingEvent fundraisingEvent = fundraisingEventService.getFundraisingEventById(fundraisingEventId);
        if (!existingBox.isRented()) {
            existingBox.setRented(true);
            existingBox.setStartDate(LocalDate.now());
            existingBox.setEndDate(LocalDate.now().plusDays(days));
            existingBox.setFundraisingEvent(fundraisingEvent);

            return boxRepository.save(existingBox);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot rent rented box!");
    }

    public void addToBox(Long id, BigDecimal amount) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }
        Objects.requireNonNull(amount, "Amount is required");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than 0");
        }
        Box existingBox = getBoxById(id);
        existingBox.setSum(amount.add(existingBox.getSum()));
        boxRepository.save(existingBox);
    }
}
