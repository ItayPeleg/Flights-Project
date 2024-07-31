package com.springflights.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "airline_company_id", referencedColumnName = "id")
    private Airline airline;

    @ManyToOne
    @JoinColumn(name = "origin_country_id", referencedColumnName = "id")
    private Country originCountry;

    @ManyToOne
    @JoinColumn(name = "destination_country_id", referencedColumnName = "id")
    private Country destinationCountry;

    @ManyToOne
    @JoinColumn(name = "departure_airport", referencedColumnName = "id")
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport", referencedColumnName = "id")
    private Airport arrivalAirport;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    public enum Type {
        ECONOMY, BUSINESS, FIRST
    }
    @Enumerated(EnumType.STRING)
    private Type cabinType;

    private LocalDateTime departureTime;
    private LocalDateTime landingTime;
    private int remainingTickets;
    private double price;
}

