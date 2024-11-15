package com.skylab.skyticket.business.abstracts;

import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.results.Result;
import com.skylab.skyticket.entities.Event;

import java.util.UUID;

public interface EventService {


    Result addEvent(Event event);

    DataResult<Event> getEventById(String eventId);



}
