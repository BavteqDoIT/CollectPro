package com.bavteqdoit.controller;

import com.bavteqdoit.entity.Box;
import com.bavteqdoit.service.BoxService;
import com.bavteqdoit.service.DonateService;
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

    @GetMapping
    public List<Box> getAllBoxes() {
        return boxService.findAllBoxes();
    }

    @GetMapping("/{id}")
    public Box getBoxById(@PathVariable long id) {
        return boxService.findBoxById(id);
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
}
