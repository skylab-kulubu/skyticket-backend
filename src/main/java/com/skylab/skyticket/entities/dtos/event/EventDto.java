package com.skylab.skyticket.entities.dtos.event;

import com.skylab.skyticket.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    private UUID id;

    private String name;

    private String description;

    private String location;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int capacity;

    private int attendedCount;

    private List<User> organizers;


}
