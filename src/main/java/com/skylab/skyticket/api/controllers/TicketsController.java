package com.skylab.skyticket.api.controllers;

import com.skylab.skyticket.business.abstracts.TicketService;
import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.entities.dtos.ticket.AddTicketDto;
import com.skylab.skyticket.entities.dtos.ticket.GetTicketDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
public class TicketsController {


    private final TicketService ticketService;


    public TicketsController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/addTicket")
    public ResponseEntity<?> addTicket(@RequestBody AddTicketDto addTicketDto){
        var result = ticketService.addTicket(addTicketDto);

        return ResponseEntity.status(result.getHttpStatus()).body(result);

    }

    @GetMapping("/getTicketById/{ticketId}")
    public ResponseEntity<DataResult<GetTicketDto>> getTicketById(@PathVariable String ticketId){
        var result = ticketService.getTicketById(UUID.fromString(ticketId));

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @PutMapping("/submitTicket/{ticketId}")
    public ResponseEntity<?> submitTicket(@PathVariable String ticketId){
        var result = ticketService.submitTicket(UUID.fromString(ticketId));

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }


}
