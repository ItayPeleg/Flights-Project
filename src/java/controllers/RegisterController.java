package com.springflights.controllers;

import com.springflights.model.Customer;
import com.springflights.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showRegisterPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping
    public String registerCustomer(@ModelAttribute("customer") @Valid Customer customer,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "There are errors in the form. Please fix them and try again.");
            return "register";
        }

        // Check if the username already exists
        if (customerRepository.existsByUsername(customer.getUsername())) {
            model.addAttribute("error", "Username already exists. Please choose a different username.");
            return "register";
        }

        // Encode password before saving
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);

        // Save the customer
        customerRepository.save(customer);

        model.addAttribute("message", "Registration successful. You can now login.");
        return "redirect:/login?registerSuccess";
    }
}
