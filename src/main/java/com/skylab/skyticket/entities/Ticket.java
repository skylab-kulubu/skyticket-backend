package com.skylab.skyticket.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "is_used")
    private boolean used;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "is_sent")
    private boolean isSent = false;

    /*
    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;
     */

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ElementCollection(targetClass = Option.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "ticket_options", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "option", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Option> options;



}
