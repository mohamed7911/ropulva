package com.ropulva.calendar.data.layer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private boolean isVerified = false;
}