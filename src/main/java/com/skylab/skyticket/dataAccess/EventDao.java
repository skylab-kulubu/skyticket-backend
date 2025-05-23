package com.skylab.skyticket.dataAccess;

import com.skylab.skyticket.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventDao extends JpaRepository<Event, UUID> {
}
