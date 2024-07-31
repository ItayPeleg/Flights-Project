package com.springflights.repository;

import com.springflights.model.Country;
import com.springflights.model.Flight;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends CrudRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.departureTime >= :fromDate AND f.departureTime <= :toDate")
    List<Flight> findFlightsBetweenDates(@Param("fromDate") LocalDateTime fromDate,
                                         @Param("toDate") LocalDateTime toDate);



    @Query("SELECT f FROM Flight f " +
            "JOIN f.originCountry oc " +
            "JOIN f.destinationCountry dc " +
            "JOIN f.departureAirport da " +
            "JOIN f.arrivalAirport aa " +
            "WHERE (oc.name = :origin OR da.code = :origin) " +
            "AND (dc.name = :destination OR aa.code = :destination) " +
            "AND f.departureTime >= :depDate " +
            "AND (:retDate IS NULL OR f.landingTime <= :retDate) " +
            "AND f.cabinType = :cabin")
    List<Flight> findFlights(@Param("origin") String origin,
                             @Param("destination") String destination,
                             @Param("depDate") LocalDateTime depDate,
                             @Param("retDate") LocalDateTime retDate,
                             @Param("cabin") Flight.Type cabin);

}
