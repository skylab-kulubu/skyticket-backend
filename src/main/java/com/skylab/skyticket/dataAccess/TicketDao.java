package com.skylab.skyticket.dataAccess;

import com.skylab.skyticket.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketDao extends JpaRepository<Ticket, UUID> {
    Optional<Ticket> findByOwnerIdAndEventId(UUID userId, UUID eventId);
}
