package com.skylab.skyticket.entities.dtos.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTicketDto {

    private String eventId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private LocalDate birthDate;

    private String university;

    private String faculty;

    private String department;

    private String view;

    private String favouriteCharacter;



}
