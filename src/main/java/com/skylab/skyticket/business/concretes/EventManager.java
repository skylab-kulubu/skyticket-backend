package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.EventService;
import com.skylab.skyticket.business.abstracts.UserService;
import com.skylab.skyticket.business.constants.Messages;
import com.skylab.skyticket.core.results.*;
import com.skylab.skyticket.core.utilities.excel.ExcelExportHelper;
import com.skylab.skyticket.dataAccess.EventDao;
import com.skylab.skyticket.entities.Event;
import com.skylab.skyticket.entities.dtos.event.EventDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class EventManager implements EventService {

    private final EventDao eventDao;

    private final UserService userService;


    public EventManager(EventDao eventDao, UserService userService) {
        this.eventDao = eventDao;
        this.userService = userService;
    }

    @Override
    public Result addEvent(Event event) {
        var authenticatedUserResult = userService.getAuthenticatedUser();
        if (!authenticatedUserResult.isSuccess()){
            return authenticatedUserResult;
        }

        if (event.getName()==null || event.getName().isEmpty()){
           return new ErrorResult(Messages.eventNameCannotBeNull, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        event.setOrganizers(List.of(authenticatedUserResult.getData()));

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

    @Override
    public DataResult<List<EventDto>> getAllEvents() {
        var result = eventDao.findAll();

        if (result.isEmpty()){
            return new ErrorDataResult<>(Messages.eventNotFound, HttpStatus.NOT_FOUND);
        }

        List<EventDto> eventDtos = result.stream().map(event -> EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .location(event.getLocation())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .capacity(event.getCapacity())
                .attendedCount(event.getParticipants().size())
                .organizers(event.getOrganizers()
        ).build()).toList();

        return new SuccessDataResult<>(eventDtos, Messages.eventsFound, HttpStatus.OK);

    }

    @Override
    public byte[] exportEventAttendeesToExcel(String eventId) throws IOException, NoSuchFieldException, IllegalAccessException {
        var eventResult = getEventById(eventId);
        if (!eventResult.isSuccess()){
            return null;
        }

        var event = eventResult.getData();

        ExcelExportHelper excelExportHelper = new ExcelExportHelper(Arrays.asList("firstName","lastName", "birthDate", "email", "phoneNumber", "university", "faculty", "department","grade"), event);
        byte[] excelFile = excelExportHelper.exportDataToExcel();

        return excelFile;
    }

}
