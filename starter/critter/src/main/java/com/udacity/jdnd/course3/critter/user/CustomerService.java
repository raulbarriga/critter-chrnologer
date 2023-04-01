package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PetService petService;

    @Autowired
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

        long customerId = pet.getOwner().getId();
        Customer customer = getCustomer(customerId);
        if (customer == null) {
            throw new NotFoundException("Customer not found with id " + customerId);
        }

        return copyCustomerToDTO(customer);
    }

    public Customer getCustomer(long customerId){
        return customerRepository.getOne(customerId);
    }

    private Customer copyCustomerDTOToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);

        List<Long> petIds = customerDTO.getPetIds();
        if (petIds != null && !petIds.isEmpty()) {
            List<Pet> petsList = new ArrayList<>();

            for (Long petId : petIds) {
                petsList.add(petService.getPet(petId));
            }
            // dto saves petIds, entity saves the whole Pet object
            customer.setPets(petsList);
        }


        return customer;
    }

    private CustomerDTO copyCustomerToDTO(Customer customer){
        CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(customer, dto);

        List<Pet> petsList = new ArrayList<>(customer.getPets());
        List<Long> petIdsList = new ArrayList<>();
        for (Pet pet : petsList) {
            petIdsList.add(pet.getId());
        }
        // dto saves petIds, entity saves the whole Pet object
        dto.setPetIds(petIdsList);

        return dto;
    }
}
