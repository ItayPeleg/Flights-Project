package com.springflights.controllers;

import com.springflights.model.*;
import com.springflights.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/adminPanel")
public class AdminController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping
    public String showAdminPage(Model model, @RequestParam(value = "message", required = false) String message) {
        List<Flight> flights = (List<Flight>) flightRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Map<String, Object>> formattedFlights = flights.stream()
                .map(flight -> {
                    Map<String, Object> flightData = new HashMap<>();
                    flightData.put("flight", flight);
                    flightData.put("formattedDepartureTime", flight.getDepartureTime() != null ? flight.getDepartureTime().format(formatter) : "N/A");
                    flightData.put("formattedLandingTime", flight.getLandingTime() != null ? flight.getLandingTime().format(formatter) : "N/A");
                    return flightData;
                })
                .collect(Collectors.toList());

        List<Customer> users = (List<Customer>) customerRepository.findAll();

        model.addAttribute("flights", formattedFlights);
        model.addAttribute("users", users);
        model.addAttribute("newUser", new Customer());
        model.addAttribute("newFlight", new Flight());
        model.addAttribute("todayDate", LocalDate.now().atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

        if (message != null) {
            model.addAttribute("message", message);
        }

        return "adminPanel";
    }

    @PostMapping("/users/add")
    public String addUser(@Valid @ModelAttribute("newUser") Customer user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Flight> flights = (List<Flight>) flightRepository.findAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            List<Map<String, Object>> formattedFlights = flights.stream()
                    .map(flight -> {
                        Map<String, Object> flightData = new HashMap<>();
                        flightData.put("flight", flight);
                        flightData.put("formattedDepartureTime", flight.getDepartureTime() != null ? flight.getDepartureTime().format(formatter) : "N/A");
                        flightData.put("formattedLandingTime", flight.getLandingTime() != null ? flight.getLandingTime().format(formatter) : "N/A");
                        return flightData;
                    })
                    .collect(Collectors.toList());

            List<Customer> users = (List<Customer>) customerRepository.findAll();

            model.addAttribute("flights", formattedFlights);
            model.addAttribute("users", users);
            model.addAttribute("newFlight", new Flight());

            model.addAttribute("message", "Error adding user. Please correct the errors and try again.");
            return "adminPanel";
        }
        customerRepository.save(user);
        return "redirect:/adminPanel?message=User added successfully";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(
            @PathVariable Long id,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String address,
            @RequestParam String phoneNo,
            @RequestParam String email) {

        Customer user = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        user.setPhoneNo(phoneNo);
        user.setEmail(email);

        customerRepository.save(user);

        return "redirect:/adminPanel?message=User updated successfully";
    }


    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        Customer user = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Find and delete purchases associated with the user
        List<Purchase> purchases = purchaseRepository.findByCustomerId(id);
        purchaseRepository.deleteAll(purchases);

        // Delete the user
        customerRepository.deleteById(id);

        return "redirect:/adminPanel?message=User deleted successfully";
    }

    @PostMapping("/flights/add")
    public String addFlight(@Valid @ModelAttribute("newFlight") Flight flight, BindingResult result,
                            @RequestParam String airlineName, @RequestParam String airlineCountryName,
                            @RequestParam String originCountryName, @RequestParam String destinationCountryName,
                            @RequestParam String departureAirportCode, @RequestParam String departureAirportName,
                            @RequestParam String arrivalAirportCode, @RequestParam String arrivalAirportName,
                            Model model) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }

            // Fetch additional data for the model
            List<Customer> users = (List<Customer>) customerRepository.findAll();
            List<Flight> flights = (List<Flight>) flightRepository.findAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            List<Map<String, Object>> formattedFlights = flights.stream()
                    .map(f -> {
                        Map<String, Object> flightData = new HashMap<>();
                        flightData.put("flight", f);
                        flightData.put("formattedDepartureTime", f.getDepartureTime() != null ? f.getDepartureTime().format(formatter) : "N/A");
                        flightData.put("formattedLandingTime", f.getLandingTime() != null ? f.getLandingTime().format(formatter) : "N/A");
                        return flightData;
                    })
                    .collect(Collectors.toList());

            model.addAttribute("flights", formattedFlights);
            model.addAttribute("users", users);
            model.addAttribute("message", "Error adding flight. Please correct the errors and try again.");
            return "adminPanel";
        }

        // Check and create Airline Country
        Country airlineCountry = countryRepository.findByName(airlineCountryName);
        if (airlineCountry == null) {
            airlineCountry = new Country();
            airlineCountry.setName(airlineCountryName);
            countryRepository.save(airlineCountry);
        }

        // Check and create Airline
        Airline airline = airlineRepository.findByName(airlineName);
        if (airline == null) {
            airline = new Airline();
            airline.setName(airlineName);
            airline.setCountry(airlineCountry);
            airlineRepository.save(airline);
        }

        // Check and create Origin Country
        Country originCountry = countryRepository.findByName(originCountryName);
        if (originCountry == null) {
            originCountry = new Country();
            originCountry.setName(originCountryName);
            countryRepository.save(originCountry);
        }

        // Check and create Destination Country
        Country destinationCountry = countryRepository.findByName(destinationCountryName);
        if (destinationCountry == null) {
            destinationCountry = new Country();
            destinationCountry.setName(destinationCountryName);
            countryRepository.save(destinationCountry);
        }

        // Check and create Departure Airport
        Airport departureAirport = airportRepository.findByCode(departureAirportCode);
        if (departureAirport == null) {
            departureAirport = new Airport();
            departureAirport.setCode(departureAirportCode);
            departureAirport.setName(departureAirportName);
            departureAirport.setCountry(originCountry);
            airportRepository.save(departureAirport);
        }

        // Check and create Arrival Airport
        Airport arrivalAirport = airportRepository.findByCode(arrivalAirportCode);
        if (arrivalAirport == null) {
            arrivalAirport = new Airport();
            arrivalAirport.setCode(arrivalAirportCode);
            arrivalAirport.setName(arrivalAirportName);
            arrivalAirport.setCountry(destinationCountry);
            airportRepository.save(arrivalAirport);
        }

        flight.setAirline(airline);
        flight.setOriginCountry(originCountry);
        flight.setDestinationCountry(destinationCountry);
        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);

        flightRepository.save(flight);
        return "redirect:/adminPanel?message=Flight added successfully";
    }




    @PostMapping("/flights/edit/{id}")
    public String updateFlight(@PathVariable Long id, @RequestParam String departureTime, @RequestParam String cabinType, @RequestParam int remainingTickets) {
        Flight flight = flightRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid flight Id:" + id));
        flight.setDepartureTime(LocalDateTime.parse(departureTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        flight.setCabinType(Flight.Type.valueOf(cabinType));
        flight.setRemainingTickets(remainingTickets);
        flightRepository.save(flight);
        return "redirect:/adminPanel?message=Flight updated successfully";
    }

    @GetMapping("/flights/delete/{id}")
    public String deleteFlight(@PathVariable Long id) {
        flightRepository.deleteById(id);
        return "redirect:/adminPanel?message=Flight deleted successfully";
    }
}
