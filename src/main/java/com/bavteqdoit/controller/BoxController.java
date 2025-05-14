package com.bavteqdoit.controller;

import com.bavteqdoit.entity.Box;
import com.bavteqdoit.service.BoxService;
import com.bavteqdoit.service.DonateService;
import com.bavteqdoit.service.TransferService;
import lombok.RequiredArgsConstructor;
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
    public Box createBox(@RequestBody Box box) {
        return boxService.createBox(box);
    }

    @PutMapping("/donate/{boxId}/{acronym}/{amount}")
    public void donateBox(@PathVariable long boxId,@PathVariable String acronym,@PathVariable BigDecimal amount) {
        donateService.donate(boxId, acronym, amount);
    }

    @PutMapping("/{id}")
    public Box updateBox(@PathVariable long id, @RequestBody Box box) {
        return boxService.updateBox(id, box);
    }

    @DeleteMapping("/{id}")
    public List<Box> deleteBox(@PathVariable long id) {
        return boxService.deleteBox(id);
    }

    @PutMapping("/rent/{id}/{days}/{fundraisingEventId}")
    public Box rentBox(@PathVariable long id, @PathVariable int days, @PathVariable long fundraisingEventId) {
        return boxService.rentBox(id,days, fundraisingEventId);
    }

    @PutMapping("/transfer/{boxId}")
    public Box transferMoneyToAccount(@PathVariable long boxId) {
        return transferService.transferMoneyToAccount(boxId);
    }

    @PutMapping("/stoprent/{boxId}")
    public Box stoprentBox(@PathVariable long boxId) {
        return transferService.stopRent(boxId);
    }
}
