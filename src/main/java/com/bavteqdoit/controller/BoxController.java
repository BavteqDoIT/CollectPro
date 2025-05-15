package com.bavteqdoit.controller;

import com.bavteqdoit.entity.Box;
import com.bavteqdoit.service.BoxService;
import com.bavteqdoit.service.DonateService;
import com.bavteqdoit.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/box")
@RequiredArgsConstructor
public class BoxController {
    private final BoxService boxService;
    private final DonateService donateService;
    private final TransferService transferService;

    @GetMapping
    public List<Box> getAllBoxes() {
        return boxService.getAllBoxes();
    }

    @GetMapping("/{id}")
    public Box getBoxById(@PathVariable long id) {
        return boxService.getBoxById(id);
    }

    @PostMapping
    public Box createBox(@Valid @RequestBody Box box) {
        return boxService.createBox(box);
    }

    @PutMapping("/donate/box-id/{boxId}/currency/{acronym}/{amount}")
    public void donateBox(@PathVariable long boxId,
                          @PathVariable String acronym,
                          @PathVariable BigDecimal amount) {
        donateService.donate(boxId, acronym, amount);
    }

    @PutMapping("/{id}")
    public Box updateBox(@PathVariable long id,
                         @Valid @RequestBody Box box) {
        return boxService.updateBox(id, box);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBox(@PathVariable long id) {
        boxService.deleteBox(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/rent/box-id/{id}/days/{days}/event/{fundraisingEventId}")
    public Box rentBox(@PathVariable long id,
                       @PathVariable int days,
                       @PathVariable long fundraisingEventId) {
        return boxService.rentBox(id, days, fundraisingEventId);
    }

    @PutMapping("/transfer/{boxId}")
    public Box transferMoneyToAccount(@PathVariable long boxId) {
        return transferService.transferMoneyToAccount(boxId);
    }

    @PutMapping("/end-rental/{boxId}")
    public Box endRental(@PathVariable long boxId) {
        return transferService.endRental(boxId);
    }
}
