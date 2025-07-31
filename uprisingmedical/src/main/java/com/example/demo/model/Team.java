// Team.java
package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamJoinRequest> requests;

    // getters and setters
}
