package com.skylab.skyticket.dataAccess;

import com.skylab.skyticket.entities.EventDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventDayDao extends JpaRepository<EventDay, UUID> {
    Optional<List<EventDay>> findAllByEventId(UUID eventId);
}
