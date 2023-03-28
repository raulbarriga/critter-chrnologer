package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.CustomerService;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;

    public PetController(CustomerService customerService, EmployeeService employeeService) {
        this.petService = petService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        return petService.createCustomer(petDTO);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping
    public List<PetDTO> getPets(){
        throw new UnsupportedOperationException();
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        throw new UnsupportedOperationException();
    }
}
