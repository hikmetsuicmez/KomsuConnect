package com.hikmetsuicmez.komsu_connect.entity;

import com.hikmetsuicmez.komsu_connect.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private User user;

        private Double totalPrice;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private OrderStatus status;

        private LocalDateTime createdAt;

        private String paymentId;
        private String paymentStatus;
        private LocalDateTime paymentDate;

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderItem> orderItems = new ArrayList<>();
}
