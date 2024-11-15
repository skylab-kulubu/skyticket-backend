package com.skylab.skyticket.business.abstracts;

import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.results.Result;
import com.skylab.skyticket.entities.Ticket;
import com.skylab.skyticket.entities.dtos.ticket.AddTicketDto;
import com.skylab.skyticket.entities.dtos.ticket.GetTicketDto;
import com.skylab.skyticket.entities.dtos.ticket.SubmitTicketDto;

import javax.xml.crypto.Data;
import java.util.UUID;

public interface TicketService {

    Result addTicket(AddTicketDto addTicketDto);

    Result submitTicket(UUID ticketId);

    DataResult<Ticket> getTicketByUserIdAndEventId(UUID userId, UUID eventId);

    DataResult<GetTicketDto> getTicketById(UUID ticket);


}
