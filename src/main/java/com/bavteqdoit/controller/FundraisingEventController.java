package com.bavteqdoit.controller;

import com.bavteqdoit.entity.FundraisingEvent;
import com.bavteqdoit.service.FundraisingEventService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/{accountId}/{organizationId}")
    public FundraisingEvent createFundraisingEvent(@PathVariable Long accountId, @PathVariable Long organizationId, @RequestBody FundraisingEvent fundraisingEvent) {
        return fundraisingEventService.createFundraisingEvent(accountId, organizationId, fundraisingEvent);
    }
}
