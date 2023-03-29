package com.udacity.jdnd.course3.critter.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String notes;

    // "owner_id" is the variable field from the PetDTO
    @OneToMany(mappedBy = "owner_id", cascade = CascadeType.ALL)
    private List<Long> petIds;
    // Lombok generates the constructors along with the getters/setters
}
