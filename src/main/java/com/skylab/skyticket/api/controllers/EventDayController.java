package com.skylab.skyticket.api.controllers;

import com.skylab.skyticket.business.abstracts.EventDayService;
import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.entities.EventDay;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/event-days")
public class EventDayController {

    private final EventDayService eventDayService;

    public EventDayController(EventDayService eventDayService) {
        this.eventDayService = eventDayService;
    }


   @PostMapping("/addEventDay/{eventId}")
    public ResponseEntity<DataResult<?>> addEventDay(@RequestBody EventDay eventDay, @PathVariable UUID eventId){
        eventDay.getEvent().setId(eventId);
        var result = eventDayService.addEventDay(eventDay);

        return ResponseEntity.status(result.getHttpStatus()).body(result);

    }

    @GetMapping("/getEventDayById/{id}")
    public ResponseEntity<DataResult<EventDay>> getEventDayById(@PathVariable UUID id){
        var result = eventDayService.getEventDayById(id);

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @GetMapping("/getEventDaysByEventId/{eventId}")
    public ResponseEntity<DataResult<List<EventDay>>> getEventDaysByEventId(@PathVariable UUID eventId){
        var result = eventDayService.getEventDaysByEventId(eventId);

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }
}
