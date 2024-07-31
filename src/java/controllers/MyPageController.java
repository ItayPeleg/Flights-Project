package com.springflights.controllers;

import com.springflights.model.Customer;
import com.springflights.model.Flight;
import com.springflights.model.Purchase;
import com.springflights.model.Ticket;
import com.springflights.repository.CustomerRepository;
import com.springflights.repository.FlightRepository;
import com.springflights.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping
    public String getMyPage(Model model, Authentication authentication, @RequestParam(value = "message", required = false) String message) {
        String username = authentication.getName();
        Customer user = customerRepository.findByUsername(username);

        // Fetch purchases with customer and ticket information
        List<Purchase> purchases = purchaseRepository.findByCustomer(user);

        model.addAttribute("user", user);
        model.addAttribute("purchases", purchases);

        if (message != null) {
            model.addAttribute("message", message);
        }

        for (Purchase purchase : purchases) {
            System.out.println("Purchase ID: " + purchase.getId());
            System.out.println("Tickets: " + purchase.getTickets().size());
            for (Ticket ticket : purchase.getTickets()) {
                System.out.println("Ticket Flight Origin: " + ticket.getFlight().getOriginCountry().getName());
                System.out.println("Ticket Flight Destination: " + ticket.getFlight().getDestinationCountry().getName());
                System.out.println("Ticket Flight Departure Time: " + ticket.getFlight().getDepartureTime());
            }
        }

        return "myPage";
    }

    @PostMapping("/edit/{id}")
    public String editUserDetails(@PathVariable("id") Long id,
                                  @RequestParam("firstName") String firstName,
                                  @RequestParam("lastName") String lastName,
                                  @RequestParam("address") String address,
                                  @RequestParam("phoneNo") String phoneNo,
                                  @RequestParam("email") String email,
                                  Model model) {
        Customer user = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        user.setPhoneNo(phoneNo);
        user.setEmail(email);
        customerRepository.save(user);

        return "redirect:/myPage?message=Your details have been successfully updated.";
    }


    @PostMapping("/cancelPurchase/{id}")
    public String cancelPurchase(@PathVariable("id") Long purchaseId, Model model) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid purchase Id:" + purchaseId));

        // Increase the remaining tickets count for each flight in the purchase
        for (Ticket ticket : purchase.getTickets()) {
            Flight flight = ticket.getFlight();
            flight.setRemainingTickets(flight.getRemainingTickets() + 1);
            flightRepository.save(flight);
        }

        // Delete the purchase
        purchaseRepository.delete(purchase);

        return "redirect:/myPage?message=Purchase canceled successfully.";
    }

}
