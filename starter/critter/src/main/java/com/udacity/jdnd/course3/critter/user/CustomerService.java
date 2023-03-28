package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;

    public CustomerService(CustomerRepository customerRepository, PetRepository petRepository) {
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = copyCustomerDTOToEntity(customerDTO);
        customer = customerRepository.save(customer);

        return copyCustomerToDTO(customer);
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerDTO customerDTO = copyCustomerToDTO(customer);
            customerDTOs.add(customerDTO);
        }

        return customerDTOs;
    }

    private Customer copyCustomerDTOToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);

        List<Pet> pets = new ArrayList<>();
        for (Long petId : customerDTO.getPetIds()) {
            pets.add(petRepository.findById(petId)
                    .orElseThrow(() -> new EntityNotFoundException("Pet with id " + petId + " not found")));
        }
        customer.setPets(pets);

        return customer;
    }

    private CustomerDTO copyCustomerToDTO(Customer customer){
        CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(customer, dto);
        customer.getPets().forEach( pet -> dto.getPetIds().add(pet.getId()));

        return dto;
    }
}
