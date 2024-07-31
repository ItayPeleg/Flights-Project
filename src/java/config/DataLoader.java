package com.springflights.config;

import com.springflights.model.*;
import com.springflights.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataLoader {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PasswordEncoder PasswordEncoder;

    @Bean
    public ApplicationRunner loadData() {
        return args -> {
            // Check if any country exists to decide if initial data load is needed
            List<Country> countries = (List<Country>) countryRepository.findAll();
            if (!countries.isEmpty()) {
                System.out.println("Data already exists. Skipping initial load.");
                return;
            }

            // Create and save countries
            Country usa = new Country(null, "USA");
            Country france = new Country(null, "France");
            Country germany = new Country(null, "Germany");
            Country canada = new Country(null, "Canada");
            Country japan = new Country(null, "Japan");
            countryRepository.saveAll(List.of(usa, france, germany, canada, japan));

            // Create and save airlines
            Airline delta = new Airline(null, "Delta Airlines", usa);
            Airline airFrance = new Airline(null, "Air France", france);
            Airline lufthansa = new Airline(null, "Lufthansa", germany);
            Airline airCanada = new Airline(null, "Air Canada", canada);
            Airline ana = new Airline(null, "All Nippon Airways", japan);
            airlineRepository.saveAll(List.of(delta, airFrance, lufthansa, airCanada, ana));

            // Create and save customers
            Customer customer1 = new Customer(null, "admin", "admin", "123 Elm Street", "555-1234", "admin", PasswordEncoder.encode("admin"), "johndoe@example.com");
            Customer customer2 = new Customer(null, "Jane", "Smith", "456 Oak Avenue", "555-5678", "janesmith", PasswordEncoder.encode("password456"), "janesmith@example.com");
            Customer customer3 = new Customer(null, "Alice", "Johnson", "789 Pine Road", "555-8765", "alicejohnson", PasswordEncoder.encode("password789"), "alicejohnson@example.com");
            Customer customer4 = new Customer(null, "Bob", "Brown", "321 Maple Street", "555-4321", "bobbrown", PasswordEncoder.encode("password012"), "bobbrown@example.com");
            Customer customer5 = new Customer(null, "Charlie", "Green", "654 Cedar Lane", "555-6789", "charliegreen", PasswordEncoder.encode("password345"), "charliegreen@example.com");
            Customer customer6 = new Customer(null, "David", "Wilson", "987 Birch Blvd", "555-3456", "davidwilson", PasswordEncoder.encode("password678"), "davidwilson@example.com");
            Customer customer7 = new Customer(null, "Emily", "Davis", "654 Spruce Street", "555-2345", "emilydavis", PasswordEncoder.encode("password901"), "emilydavis@example.com");
            Customer customer8 = new Customer(null, "Frank", "Miller", "321 Elm Street", "555-7890", "frankmiller", PasswordEncoder.encode("password234"), "frankmiller@example.com");
            Customer customer9 = new Customer(null, "Grace", "Martinez", "456 Pine Avenue", "555-1234", "gracemartinez", PasswordEncoder.encode("password567"), "gracemartinez@example.com");
            Customer customer10 = new Customer(null, "Henry", "Moore", "789 Maple Road", "555-6789", "henrymoore", PasswordEncoder.encode("password890"), "henrymoore@example.com");
            customerRepository.saveAll(List.of(customer1, customer2, customer3, customer4, customer5, customer6, customer7, customer8, customer9, customer10));

            // Create and save airports
            Airport jfk = new Airport(null, "JFK", "John F. Kennedy International Airport", usa);
            Airport cdg = new Airport(null, "CDG", "Charles de Gaulle Airport", france);
            Airport fra = new Airport(null, "FRA", "Frankfurt Airport", germany);
            Airport yvr = new Airport(null, "YVR", "Vancouver International Airport", canada);
            Airport hnd = new Airport(null, "HND", "Tokyo Haneda Airport", japan);
            airportRepository.saveAll(List.of(jfk, cdg, fra, yvr, hnd));

            // Create and save flights with past and future dates directly
            Flight flight1 = new Flight(null, delta, usa, france, jfk, cdg, null, Flight.Type.ECONOMY, LocalDateTime.of(2024, 6, 1, 10, 0), LocalDateTime.of(2024, 6, 1, 18, 0), 50, 100.0);
            Flight flight2 = new Flight(null, airFrance, france, usa, cdg, jfk, null, Flight.Type.BUSINESS, LocalDateTime.of(2024, 5, 15, 12, 0), LocalDateTime.of(2024, 5, 15, 19, 0), 75, 200.0);
            Flight flight3 = new Flight(null, lufthansa, germany, canada, fra, yvr, null, Flight.Type.FIRST, LocalDateTime.of(2024, 8, 15, 16, 0), LocalDateTime.of(2024, 8, 15, 22, 0), 80, 300.0);
            Flight flight4 = new Flight(null, airCanada, canada, japan, yvr, hnd, null, Flight.Type.ECONOMY, LocalDateTime.of(2024, 8, 25, 18, 0), LocalDateTime.of(2024, 8, 25, 23, 0), 90, 150.0);
            Flight flight5 = new Flight(null, ana, japan, usa, hnd, jfk, null, Flight.Type.BUSINESS, LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(5).plusHours(12), 60, 250.0);
            Flight flight6 = new Flight(null, delta, france, germany, cdg, fra, null, Flight.Type.ECONOMY, LocalDateTime.of(2024, 4, 20, 11, 0), LocalDateTime.of(2024, 4, 20, 18, 0), 55, 110.0);
            Flight flight7 = new Flight(null, airFrance, germany, canada, fra, yvr, null, Flight.Type.FIRST, LocalDateTime.of(2024, 9, 10, 15, 0), LocalDateTime.of(2024, 9, 10, 21, 0), 85, 320.0);
            Flight flight8 = new Flight(null, lufthansa, canada, japan, yvr, hnd, null, Flight.Type.ECONOMY, LocalDateTime.of(2024, 3, 5, 13, 0), LocalDateTime.of(2024, 3, 5, 20, 0), 65, 140.0);
            Flight flight9 = new Flight(null, airCanada, japan, usa, hnd, jfk, null, Flight.Type.BUSINESS, LocalDateTime.of(2024, 10, 15, 17, 0), LocalDateTime.of(2024, 10, 15, 23, 0), 70, 260.0);
            Flight flight10 = new Flight(null, ana, usa, france, jfk, cdg, null, Flight.Type.FIRST, LocalDateTime.now().plusDays(15), LocalDateTime.now().plusDays(15).plusHours(8), 75, 270.0);

            flightRepository.saveAll(List.of(flight1, flight2, flight3, flight4, flight5, flight6, flight7, flight8, flight9, flight10));

            // Create and save purchases with total amounts
            Purchase purchase1 = new Purchase(null, customer1, new ArrayList<>(), "1234-5678-9012-3456", LocalDateTime.now(), flight1.getPrice());
            Purchase purchase2 = new Purchase(null, customer2, new ArrayList<>(), "2345-6789-0123-4567", LocalDateTime.now(), flight2.getPrice());
            Purchase purchase3 = new Purchase(null, customer3, new ArrayList<>(), "3456-7890-1234-5678", LocalDateTime.now(), flight3.getPrice());
            Purchase purchase4 = new Purchase(null, customer4, new ArrayList<>(), "4567-8901-2345-6789", LocalDateTime.now(), flight4.getPrice());
            Purchase purchase5 = new Purchase(null, customer5, new ArrayList<>(), "5678-9012-3456-7890", LocalDateTime.now(), flight5.getPrice());
            Purchase purchase6 = new Purchase(null, customer6, new ArrayList<>(), "6789-0123-4567-8901", LocalDateTime.now(), flight6.getPrice());
            Purchase purchase7 = new Purchase(null, customer7, new ArrayList<>(), "7890-1234-5678-9012", LocalDateTime.now(), flight7.getPrice());
            Purchase purchase8 = new Purchase(null, customer8, new ArrayList<>(), "8901-2345-6789-0123", LocalDateTime.now(), flight8.getPrice());
            Purchase purchase9 = new Purchase(null, customer9, new ArrayList<>(), "9012-3456-7890-1234", LocalDateTime.now(), flight9.getPrice());
            Purchase purchase10 = new Purchase(null, customer10, new ArrayList<>(), "0123-4567-8901-2345", LocalDateTime.now(), flight10.getPrice());


            purchaseRepository.saveAll(List.of(purchase1, purchase2, purchase3, purchase4, purchase5, purchase6, purchase7, purchase8, purchase9, purchase10));

            // Create and save tickets
            Ticket ticket1 = new Ticket(null, flight1, customer1, purchase1);
            Ticket ticket2 = new Ticket(null, flight2, customer2, purchase2);
            Ticket ticket3 = new Ticket(null, flight3, customer3, purchase3);
            Ticket ticket4 = new Ticket(null, flight4, customer4, purchase4);
            Ticket ticket5 = new Ticket(null, flight5, customer5, purchase5);
            Ticket ticket6 = new Ticket(null, flight6, customer6, purchase6);
            Ticket ticket7 = new Ticket(null, flight7, customer7, purchase7);
            Ticket ticket8 = new Ticket(null, flight8, customer8, purchase8);
            Ticket ticket9 = new Ticket(null, flight9, customer9, purchase9);
            Ticket ticket10 = new Ticket(null, flight10, customer10, purchase10);

            ticketRepository.saveAll(List.of(ticket1, ticket2, ticket3, ticket4, ticket5, ticket6, ticket7, ticket8, ticket9, ticket10));

            // Update purchases with tickets and calculate total amounts
            purchase1.setTickets(List.of(ticket1));
            purchase1.setTotalAmount(ticket1.getFlight().getPrice()); // Update total amount

            purchase2.setTickets(List.of(ticket2));
            purchase2.setTotalAmount(ticket2.getFlight().getPrice()); // Update total amount

            purchase3.setTickets(List.of(ticket3));
            purchase3.setTotalAmount(ticket3.getFlight().getPrice()); // Update total amount

            purchase4.setTickets(List.of(ticket4));
            purchase4.setTotalAmount(ticket4.getFlight().getPrice()); // Update total amount

            purchase5.setTickets(List.of(ticket5));
            purchase5.setTotalAmount(ticket5.getFlight().getPrice()); // Update total amount

            purchase6.setTickets(List.of(ticket6));
            purchase6.setTotalAmount(ticket6.getFlight().getPrice()); // Update total amount

            purchase7.setTickets(List.of(ticket7));
            purchase7.setTotalAmount(ticket7.getFlight().getPrice()); // Update total amount

            purchase8.setTickets(List.of(ticket8));
            purchase8.setTotalAmount(ticket8.getFlight().getPrice()); // Update total amount

            purchase9.setTickets(List.of(ticket9));
            purchase9.setTotalAmount(ticket9.getFlight().getPrice()); // Update total amount

            purchase10.setTickets(List.of(ticket10));
            purchase10.setTotalAmount(ticket10.getFlight().getPrice()); // Update total amount

            purchaseRepository.saveAll(List.of(purchase1, purchase2, purchase3, purchase4, purchase5, purchase6, purchase7, purchase8, purchase9, purchase10));
        };
    }
}
