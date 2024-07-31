package com.springflights.controllers;

import com.springflights.model.Flight;
import com.springflights.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/flights")
public class FlightController {
    @Autowired
    private FlightRepository flightRepository;

    @GetMapping
    public String showFlightsPage(Model model) {
        return "flights";
    }

    @ModelAttribute
    public String showFlights(Model model) {
        Iterable<Flight> flights = flightRepository.findAll();
        model.addAttribute("flights", flights);
        model.addAttribute("todayDate", LocalDate.now().toString());
        return "redirect:/flights";
    }

    @GetMapping("/searchFlights")
    public String showSearchPage(Model model) {
        // Add an empty list to the model
        model.addAttribute("flights", Collections.emptyList());

        return "searchFlights";
    }

    @PostMapping("/search")
    public String searchFlights(@RequestParam("origin") String origin,
                                @RequestParam("destination") String destination,
                                @RequestParam("departureDate") String departureDate,
                                @RequestParam(value = "returnDate", required = false) String returnDate,
                                @RequestParam("cabin") Flight.Type cabinType,
                                @RequestParam("adults") int adults,
                                @RequestParam("children") int children,
                                @RequestParam("infants") int infants,
                                Model model) {

        System.out.println("Adults: " + adults);
        System.out.println("Children: " + children);
        System.out.println("Infants: " + infants);

        // Parse date strings to LocalDateTime if needed
        LocalDateTime depDateTime = LocalDateTime.parse(departureDate + "T00:00:00");
        LocalDateTime retDateTime = (returnDate != null && !returnDate.isEmpty()) ?
                LocalDateTime.parse(returnDate + "T00:00:00") : null;

        // Calculate total tickets
        int totalTickets = adults + children + infants;

        // Query flights based on search parameters
        List<Flight> flights = flightRepository.findFlights(origin, destination, depDateTime, retDateTime, cabinType);

        // Format LocalDateTime to string for Thymeleaf
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<Map<String, Object>> formattedFlights = flights.stream()
                .map(flight -> {
                    Map<String, Object> flightData = new HashMap<>();
                    flightData.put("flight", flight);
                    flightData.put("formattedDepartureTime", flight.getDepartureTime().format(formatter));
                    flightData.put("totalTickets", totalTickets); // Include totalTickets in each flight entry
                    return flightData;
                })
                .collect(Collectors.toList());

        // Add formatted flights and total tickets to the model
        model.addAttribute("flights", formattedFlights);
        model.addAttribute("totalTickets", totalTickets);

        return "searchFlights";
    }


    @PostMapping("/filter")
    public String filterFlights(@RequestParam("fromDate") String fromDateStr,
                                @RequestParam("toDate") String toDateStr,
                                Model model) {

        // Parse dates from String to LocalDate
        LocalDate fromDate = LocalDate.parse(fromDateStr);
        LocalDate toDate = LocalDate.parse(toDateStr);

        // Adjust toDate to include the full day (until 23:59:59.999999999)
        LocalDateTime toDateEndOfDay = toDate.atTime(LocalTime.MAX);

        // Query flights between fromDate and toDate
        List<Flight> flights = flightRepository.findFlightsBetweenDates(fromDate.atStartOfDay(), toDateEndOfDay);

        // Add filtered flights to the model
        model.addAttribute("flights", flights);

        return "flights";
    }
}

