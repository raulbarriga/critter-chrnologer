package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class PetService {
    private final PetRepository petRepository;
    private CustomerService customerService;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    // to avoid a circular reference error between this class & the CustomerService one
    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public PetDTO createPet(PetDTO petDTO) {
        Pet pet = copyPetDTOToEntity(petDTO);
        pet = petRepository.save(pet);

        return copyPetToDTO(pet);
    }

    public PetDTO getPetById(long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet with id " + petId + " not found"));

        return copyPetToDTO(pet);
    }

    public List<PetDTO> getAllPets() {
        List<Pet> pets = petRepository.findAll();

        List<PetDTO> petDTOs = new ArrayList<>();
        for (Pet pet : pets) {
            PetDTO petDTO = copyPetToDTO(pet);
            petDTOs.add(petDTO);
        }

        return petDTOs;
    }

    public Pet getPet(long petId){
        return petRepository.getOne(petId);
    }

    public List<PetDTO> getPetsByOwnerId(long ownerId) {
        List<Pet> pets = petRepository.findByOwnerId(ownerId);

        if (pets.isEmpty()) {
            throw new EntityNotFoundException("No pets found for owner with id " + ownerId);
        }

        List<PetDTO> petDTOs = new ArrayList<>();
        for (Pet pet : pets) {
            PetDTO petDTO = copyPetToDTO(pet);
            petDTOs.add(petDTO);
        }

        return petDTOs;
    }

    private Pet copyPetDTOToEntity(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        // call get owner by id to get a `Customer` object back
        if (petDTO.getOwnerId() != 0) {
            Customer owner = customerService.getCustomer(petDTO.getOwnerId());
            pet.setOwner(owner);
            // update the owner with the new pet and save
            owner.getPets().add(pet);
        }

        return pet;
    }

    private PetDTO copyPetToDTO(Pet pet){
        PetDTO dto = new PetDTO();
        BeanUtils.copyProperties(pet, dto);
        if (pet.getOwner().getId() != 0) {
            Customer owner = customerService.getCustomer(pet.getOwner().getId());
            dto.setOwnerId(owner.getId());
        }

        return dto;
    }
}
