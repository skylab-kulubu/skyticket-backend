package com.skylab.skyticket.api.controllers;

import com.skylab.skyticket.business.abstracts.EventService;
import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.entities.Event;
import com.skylab.skyticket.entities.dtos.event.EventDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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


    @GetMapping("/getEventById/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable String eventId){
        var result = eventService.getEventById(eventId);

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @GetMapping("/getAllEvents")
    public ResponseEntity<DataResult<List<EventDto>>> getAllEvents(){
        var result = eventService.getAllEvents();

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @GetMapping("/getEventAttendeesToExcel/{eventId}")
    public ResponseEntity<?> getEventAttendeesToExcel(@PathVariable String eventId) {
        try {
            byte[] result = eventService.exportEventAttendeesToExcel(eventId);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Excel dosyası oluşturulamadı: Etkinlik bulunamadı veya katılımcı yok.");
            }
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename="+eventId+"_idli_event_katilimci_listesi"+".xlsx")
                    .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Excel dosyası oluşturulurken beklenmeyen bir hata oluştu.");
        }
    }

}
