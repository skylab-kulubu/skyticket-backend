package com.skylab.skyticket.business.abstracts;

import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.entities.EventDay;

import java.util.List;
import java.util.UUID;

public interface EventDayService {

    DataResult<?> addEventDay(EventDay eventDay);

    DataResult<EventDay> getEventDayById(UUID id);

    DataResult<List<EventDay>> getEventDaysByEventId(UUID eventId);



}
