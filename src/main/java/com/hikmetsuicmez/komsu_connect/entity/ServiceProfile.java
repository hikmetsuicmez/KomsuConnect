package com.hikmetsuicmez.komsu_connect.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private String description;
}
