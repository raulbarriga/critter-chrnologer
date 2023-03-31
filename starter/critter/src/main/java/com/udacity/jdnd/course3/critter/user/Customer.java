package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(targetEntity = Pet.class)
    private List<Pet> pets = new ArrayList<>();
    // Lombok generates the constructors along with the getters/setters
}
