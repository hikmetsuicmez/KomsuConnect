package com.hikmetsuicmez.komsu_connect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Bildirimi alan kullanıcı

    private String message; // Bildirim mesajı

    private LocalDateTime timestamp; // Bildirim zamanı

    private boolean isRead = false; // Okunup okunmadığı bilgisi
}
