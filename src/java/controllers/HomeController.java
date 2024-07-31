package com.springflights.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/home")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping
    public String showHomePage() {
        return "home";
    }

    @PostMapping("/contact")
    public String handleContactForm(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "message", required = false) String message,
            Model model) {

        // Log the values received from the form
        logger.debug("Received form data - Name: {}, Email: {}, Phone: {}, Message: {}", name, email, phone, message);

        // Print the values received from the form
        System.out.println("Received form data - Name: " + name + ", Email: " + email + ", Phone: " + phone + ", Message: " + message);

        // Check if parameters are present and not null
        if (name == null || email == null || phone == null || message == null) {
            model.addAttribute("error", "All fields are required.");
            return "home";
        }

        // Process the form data here (e.g., save to database, send an email)

        // Add a success message to the model
        model.addAttribute("message", "Thank you for contacting us, " + name + "!");
        return "home";
    }
}
