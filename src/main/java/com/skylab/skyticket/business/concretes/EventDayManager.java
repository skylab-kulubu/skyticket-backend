package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.EventDayService;
import com.skylab.skyticket.business.abstracts.EventService;
import com.skylab.skyticket.business.constants.Messages;
import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.results.ErrorDataResult;
import com.skylab.skyticket.core.results.SuccessDataResult;
import com.skylab.skyticket.dataAccess.EventDayDao;
import com.skylab.skyticket.entities.EventDay;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventDayManager implements EventDayService {

    private final EventDayDao eventDayDao;

    private final EventService eventService;

    public EventDayManager(EventDayDao eventDayDao, EventService eventService) {
        this.eventDayDao = eventDayDao;
        this.eventService = eventService;
    }

    @Override
    public DataResult<?> addEventDay(EventDay eventDay) {

        if (eventDay.getStartDate() == null || eventDay.getEndDate() == null){
            return new ErrorDataResult<>(Messages.startDateAndEndDateCannotBeNull, HttpStatus.BAD_REQUEST);
        }

        if (eventDay.getStartDate().isAfter(eventDay.getEndDate())){
           return new ErrorDataResult<>(Messages.startDateMustBeBeforeEndDate, HttpStatus.BAD_REQUEST);
        }

        var eventResult = eventService.getEventById(eventDay.getEvent().getId().toString());
        if (!eventResult.isSuccess()){
            return eventResult;
        }
        var event = eventResult.getData();

        eventDay.setEvent(event);

        return new SuccessDataResult<>(eventDayDao.save(eventDay), Messages.eventDayAdded, HttpStatus.CREATED);
    }

    @Override
    public DataResult<EventDay> getEventDayById(UUID id) {
        var eventDay = eventDayDao.findById(id);

        if (eventDay.isEmpty()){
            return new ErrorDataResult<>(Messages.eventNotFound, HttpStatus.NOT_FOUND);
        }

        return new SuccessDataResult<>(eventDay.get(), Messages.eventFound, HttpStatus.OK);
    }

    @Override
    public DataResult<List<EventDay>> getEventDaysByEventId(UUID eventId) {
        var eventDays = eventDayDao.findAllByEventId(eventId);

        if (eventDays.isEmpty()){
            return new ErrorDataResult<>(Messages.eventNotFound, HttpStatus.NOT_FOUND);
        }

        return new SuccessDataResult<>(eventDays.get(), Messages.eventFound, HttpStatus.OK);
    }
}
