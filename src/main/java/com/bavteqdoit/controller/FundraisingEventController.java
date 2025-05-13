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
        return fundraisingEventService.findAllFundraisingEvents();
    }

    @PostMapping
    public FundraisingEvent createFundraisingEvent(@RequestBody FundraisingEvent fundraisingEvent) {
        return fundraisingEventService.createFundraisingEvent(fundraisingEvent);
    }
}
