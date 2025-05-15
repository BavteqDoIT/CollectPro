package com.bavteqdoit.controller;

import com.bavteqdoit.entity.FundraisingEvent;
import com.bavteqdoit.service.FundraisingEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fundraising-events")
@RequiredArgsConstructor
public class FundraisingEventController {

    private final FundraisingEventService fundraisingEventService;

    @GetMapping
    public List<FundraisingEvent> getAllFundraisingEvents() {
        return fundraisingEventService.getAllFundraisingEvents();
    }

    @GetMapping("/{id}")
    public FundraisingEvent getFundraisingEventById(@PathVariable long id) {
        return fundraisingEventService.getFundraisingEventById(id);
    }

    @PostMapping("/account/{accountId}/organization/{organizationId}")
    public FundraisingEvent createFundraisingEvent(@PathVariable Long accountId,
                                                   @PathVariable Long organizationId,
                                                   @Valid @RequestBody FundraisingEvent fundraisingEvent) {
        return fundraisingEventService.createFundraisingEvent(accountId, organizationId, fundraisingEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFundraisingEventById(@PathVariable long id) {
        fundraisingEventService.deleteFundraisingEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public FundraisingEvent updateFundraisingEvent(@PathVariable long id,
                                                   @RequestBody FundraisingEvent fundraisingEvent) {
        return fundraisingEventService.updateFundraisingEvent(id, fundraisingEvent);
    }
}
