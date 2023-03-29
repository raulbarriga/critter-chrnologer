package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PetService petService;

    public CustomerService(CustomerRepository customerRepository, PetService petService) {
        this.customerRepository = customerRepository;
        this.petService = petService;
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

    public CustomerDTO getOwnerByPet(long petId) throws NotFoundException {
        Pet pet = petService.getPet(petId);
        if (pet == null) {
            throw new NotFoundException("Pet not found with id " + petId);
        }

        long customerId = pet.getOwnerId();
        Customer customer = getCustomer(customerId);
        if (customer == null) {
            throw new NotFoundException("Customer not found with id " + customerId);
        }

        return copyCustomerToDTO(customer);
    }

    private Customer getCustomer(long customerId){
        return customerRepository.getOne(customerId);
    }

    private Customer copyCustomerDTOToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);

        // petRepository.findById(petId)
        //                    .orElseThrow(() -> new EntityNotFoundException("Pet with id " + petId + " not found")))
        List<Long> pets = new ArrayList<>(customerDTO.getPetIds());
        customer.setPetIds(pets);

        return customer;
    }

    private CustomerDTO copyCustomerToDTO(Customer customer){
        CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(customer, dto);
        //customer.getPetIds().forEach( pet -> dto.getPetIds().add(pet.getId()));
        List<Long> petIds = new ArrayList<>(customer.getPetIds());
        dto.setPetIds(petIds);

        return dto;
    }
}
