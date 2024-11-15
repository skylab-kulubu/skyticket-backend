package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.EventService;
import com.skylab.skyticket.business.constants.Messages;
import com.skylab.skyticket.core.results.*;
import com.skylab.skyticket.dataAccess.EventDao;
import com.skylab.skyticket.entities.Event;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventManager implements EventService {

    private final EventDao eventDao;


    public EventManager(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public Result addEvent(Event event) {
        if (event.getName()==null || event.getName().isEmpty()){
           return new ErrorResult(Messages.eventNameCannotBeNull, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        eventDao.save(event);

        return new SuccessResult(Messages.eventAdded, HttpStatus.CREATED);

    }

    @Override
    public DataResult<Event> getEventById(String eventId) {
        var event = eventDao.findById(UUID.fromString(eventId));

        if (event.isEmpty()){
            return new ErrorDataResult<>(Messages.eventNotFound, HttpStatus.NOT_FOUND);
        }

        return new SuccessDataResult<>(event.get(), Messages.eventFound, HttpStatus.OK);
    }
}
