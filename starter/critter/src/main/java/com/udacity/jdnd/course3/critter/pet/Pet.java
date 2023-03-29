package com.udacity.jdnd.course3.critter.pet;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private PetType type;

    private String name;

    //@Column(name = "owner_id")

    @ManyToOne
    @JoinColumn(name = "owner_id", insertable=false, updatable=false)
    private long ownerId;

    //private Customer owner;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String notes;
    // Lombok generates the constructors along with the getters/setters
}