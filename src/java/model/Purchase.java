package com.springflights.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @NotBlank(message = "Credit card number is required")
    private String creditCardNo;

    private LocalDateTime purchaseDate;

    private double totalAmount;
}
