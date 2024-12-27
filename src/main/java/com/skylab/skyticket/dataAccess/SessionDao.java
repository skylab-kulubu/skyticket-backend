package com.skylab.skyticket.dataAccess;

import com.skylab.skyticket.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessionDao extends JpaRepository<Session, UUID> {
}
