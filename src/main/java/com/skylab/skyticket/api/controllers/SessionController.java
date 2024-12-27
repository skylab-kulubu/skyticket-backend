package com.skylab.skyticket.api.controllers;

import com.skylab.skyticket.business.abstracts.SessionService;
import com.skylab.skyticket.entities.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;


    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @PostMapping("/addSession/{eventDayId}")
    public ResponseEntity<?> addSession(@RequestBody Session session, @PathVariable UUID eventDayId) {
        session.getEventDay().setId(eventDayId);

        var result = sessionService.addSession(session);

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }
}
