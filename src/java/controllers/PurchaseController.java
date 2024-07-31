package com.springflights.controllers;

import com.springflights.model.Customer;
import com.springflights.model.Flight;
import com.springflights.model.Purchase;
import com.springflights.model.Ticket;
import com.springflights.repository.CustomerRepository;
import com.springflights.repository.FlightRepository;
import com.springflights.repository.PurchaseRepository;
import com.springflights.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
public class PurchaseController {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/purchase")
    public String showPurchasePage(@RequestParam("flightId") Long flightId,
                                   @RequestParam(value = "totalTickets", required = false) Integer totalTickets,
                                   @RequestParam("flightPrice") Double flightPrice,
                                   @RequestParam(value = "message", required = false) String message,
                                   Model model) {
        // Retrieve the flight details
        Flight flight = flightRepository.findById(flightId).orElse(null);

        if (flight == null) {
            model.addAttribute("message", "Flight not found.");
            return "error";
        }

        // Get the logged-in user's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer customer = customerRepository.findByUsername(userDetails.getUsername());

        model.addAttribute("flight", flight);
        model.addAttribute("customer", customer);
        model.addAttribute("totalTickets", totalTickets);
        model.addAttribute("flightPrice", flightPrice);
        model.addAttribute("message", message);

        return "purchase";
    }

    @PostMapping("/purchaseTicket")
    public String purchaseTicket(@RequestParam("flightId") Long flightId,
                                 @RequestParam("totalTickets") Integer totalTickets,
                                 @RequestParam("flightPrice") Double flightPrice,
                                 @RequestParam("creditCardNo") String creditCardNo,
                                 Model model) {
        // Get the logged-in user's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Customer customer = customerRepository.findByUsername(userDetails.getUsername());

        Flight flight = flightRepository.findById(flightId).orElse(null);
        if (flight == null) {
            model.addAttribute("message", "Flight not found.");
            return "redirect:/purchase?error=flightNotFound";
        }

        if (customer == null) {
            model.addAttribute("message", "Customer not found.");
            return "redirect:/purchase?error=customerNotFound";
        }

        if (flight.getRemainingTickets() < totalTickets) {
            model.addAttribute("message", "Not enough remaining tickets for this flight.");
            return "redirect:/purchase?error=notEnoughTickets";
        }

        // Create a new purchase
        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase.setTickets(new ArrayList<>()); // Initialize the ticket list
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setTotalAmount(totalTickets * flightPrice);
        purchase.setCreditCardNo(creditCardNo); // Save the credit card number

        // Save the purchase
        purchaseRepository.save(purchase);

        // Update remaining tickets
        flight.setRemainingTickets(flight.getRemainingTickets() - totalTickets);
        flightRepository.save(flight);

        // Add tickets to the purchase
        for (int i = 0; i < totalTickets; i++) {
            Ticket ticket = new Ticket();
            ticket.setFlight(flight);
            ticket.setPurchase(purchase);
            ticket.setCustomer(customer); // Set the customer here
            purchase.getTickets().add(ticket); // Update the Purchase entity
            ticketRepository.save(ticket);
        }

        // Add success message
        String message = URLEncoder.encode("Purchase confirmed successfully!", StandardCharsets.UTF_8);

        return "redirect:/purchase?flightId=" + flightId + "&totalTickets=" + totalTickets + "&flightPrice=" + flightPrice + "&message=" + message;
    }
}
