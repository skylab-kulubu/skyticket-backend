package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.EventDayService;
import com.skylab.skyticket.business.abstracts.SessionService;
import com.skylab.skyticket.business.abstracts.UserService;
import com.skylab.skyticket.business.constants.Messages;
import com.skylab.skyticket.core.results.*;
import com.skylab.skyticket.dataAccess.SessionDao;
import com.skylab.skyticket.entities.Session;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SessionManager implements SessionService {

    private final SessionDao sessionDao;

    private final EventDayService eventDayService;

    private final UserService userService;

    public SessionManager(SessionDao sessionDao, EventDayService eventDayService, UserService userService) {
        this.sessionDao = sessionDao;
        this.eventDayService = eventDayService;
        this.userService = userService;
    }

    @Override
    public DataResult<?> addSession(Session session) {

        if (session.getStartDate() == null || session.getEndDate() == null) {
            return new ErrorDataResult<>(Messages.startDateAndEndDateCannotBeNull, HttpStatus.BAD_REQUEST);
        }


        if (session.getStartDate().isAfter(session.getEndDate())) {
            return new ErrorDataResult<>(Messages.startDateMustBeBeforeEndDate, HttpStatus.BAD_REQUEST);
        }

        if (session.getName() == null){
            return new ErrorDataResult<>(Messages.sessionNameCannotBeNull, HttpStatus.BAD_REQUEST);
        }


        var eventDayResult = eventDayService.getEventDayById(session.getEventDay().getId());
        if (!eventDayResult.isSuccess()){
            return eventDayResult;
        }

        session.setEventDay(eventDayResult.getData());

        return new SuccessDataResult<>(sessionDao.save(session), Messages.sessionAdded, HttpStatus.CREATED);


    }

    @Override
    public Result deleteSession(UUID sessionId) {

        var sessionResult = getSessionById(sessionId);
        if (!sessionResult.isSuccess()){
            return sessionResult;
        }

        sessionDao.deleteById(sessionId);

        return new SuccessResult(Messages.sessionDeleted, HttpStatus.OK);
    }

    @Override
    public DataResult<Session> getSessionById(UUID id) {
        var session = sessionDao.findById(id);
        if (session.isEmpty()){
            return new ErrorDataResult<>(Messages.sessionNotFound, HttpStatus.NOT_FOUND);
        }

        return new SuccessDataResult<>(session.get(), Messages.sessionsFound, HttpStatus.OK);
    }

    @Override
    public Result addParticipantToSessionBySessionIdAndUserEmail(UUID sessionId, String userEmail) {
        var sessionResult = getSessionById(sessionId);
        if (!sessionResult.isSuccess()){
            return sessionResult;
        }

        var userResult = userService.getUserByEmail(userEmail);
        if (!userResult.isSuccess()){
            return userResult;
        }

        var session = sessionResult.getData();
        var user = userResult.getData();

        session.getParticipants().add(user);

        sessionDao.save(session);

        return new SuccessResult(Messages.userAdded, HttpStatus.OK);
    }

    @Override
    public Result addParticipantToSessionBySessionIdAndUserPhoneNumber(UUID sessionId, String userPhoneNumber) {
        var sessionResult = getSessionById(sessionId);
        if (!sessionResult.isSuccess()){
            return sessionResult;
        }

        var userResult = userService.getUserByPhoneNumber(userPhoneNumber);
        if (!userResult.isSuccess()){
            return userResult;
        }

        var session = sessionResult.getData();
        var user = userResult.getData();

        session.getParticipants().add(user);

        sessionDao.save(session);

        return new SuccessResult(Messages.userAdded, HttpStatus.OK);
    }


    @Override
    public DataResult<List<Session>> getSessionsByEventDayId(UUID eventDayId) {
        var eventDayResult = eventDayService.getEventDayById(eventDayId);
        if (!eventDayResult.isSuccess()){
            return new ErrorDataResult<>(Messages.eventDayNotFound, HttpStatus.NOT_FOUND);
        }

        return new SuccessDataResult<>(eventDayResult.getData().getSessions(), Messages.sessionsFound, HttpStatus.OK);
    }
}
