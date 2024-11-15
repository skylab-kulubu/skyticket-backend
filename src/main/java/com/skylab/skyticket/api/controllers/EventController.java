package com.skylab.skyticket.api.controllers;

import com.skylab.skyticket.business.abstracts.EventService;
import com.skylab.skyticket.entities.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @PostMapping("/addEvent")
    public ResponseEntity<?> addEvent(@RequestBody Event event){
        var result = eventService.addEvent(event);

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }
}
