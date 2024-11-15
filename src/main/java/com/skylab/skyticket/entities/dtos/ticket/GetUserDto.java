package com.skylab.skyticket.entities.dtos.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserDto {

    private UUID id;

    private UUID yildizskylabId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private String birthDate;

    private String university;

    private String faculty;

    private String department;


}
