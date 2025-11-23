package com.finance.domain;

import lombok.*;

import jakarta.persistence.*;
@Entity
@Table(name  = "role")
@Data
@AllArgsConstructor
@Getter @Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }


}