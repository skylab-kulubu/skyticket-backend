package com.skylab.skyticket.entities.dtos.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitTicketDto {

    private UUID eventId;

    private UUID ticketId;

}
