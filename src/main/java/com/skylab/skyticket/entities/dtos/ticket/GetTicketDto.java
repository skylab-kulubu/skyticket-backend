package com.skylab.skyticket.entities.dtos.ticket;


import com.skylab.skyticket.entities.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTicketDto {

    private UUID id;

    private boolean isUsed;

    private GetEventDto event;

    private GetUserDto owner;

    private Set<Option> options;

    private LocalDateTime usedAt;

}
