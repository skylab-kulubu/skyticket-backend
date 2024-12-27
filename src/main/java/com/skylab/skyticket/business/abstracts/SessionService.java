package com.skylab.skyticket.business.abstracts;

import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.results.Result;
import com.skylab.skyticket.entities.Session;

import java.util.List;
import java.util.UUID;

public interface SessionService {

    DataResult<?> addSession(Session session);

    Result deleteSession(UUID sessionId);

    DataResult<Session> getSessionById(UUID id);

    Result addParticipantToSessionBySessionIdAndUserEmail(UUID sessionId, String userEmail);

    Result addParticipantToSessionBySessionIdAndUserPhoneNumber(UUID sessionId, String userPhoneNumber);

    DataResult<List<Session>> getSessionsByEventDayId(UUID eventDayId);



}
