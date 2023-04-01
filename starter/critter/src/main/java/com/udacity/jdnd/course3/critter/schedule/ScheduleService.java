package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PetRepository petRepository;
    private final PetService petService;
    private final EmployeeService employeeService;

    public ScheduleService(ScheduleRepository scheduleRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository, PetRepository petRepository, PetService petService, EmployeeService employeeService) {
        this.scheduleRepository = scheduleRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
        this.petService = petService;
        this.employeeService = employeeService;
    }

    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = copyScheduleDTOToEntity(scheduleDTO);
        schedule = scheduleRepository.save(schedule);

        return copyScheduleToDTO(schedule);
    }

    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();

        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = copyScheduleToDTO(schedule);
            scheduleDTOs.add(scheduleDTO);
        }

        return scheduleDTOs;
    }

    public List<ScheduleDTO> getPetSchedule(long petId) {
        Pet pet = petRepository.getOne(petId);
        List<Schedule> schedules = scheduleRepository.getAllByPetsContains(pet);
        List<ScheduleDTO> schedulesDTOs = new ArrayList<>();
        for (Schedule schedule : schedules) {
            schedulesDTOs.add(copyScheduleToDTO(schedule));
        }

        return schedulesDTOs;
    }

    public List<ScheduleDTO> getEmployeeSchedule(long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        List<Schedule> schedules = scheduleRepository.getAllByEmployeesContains(employee);
        List<ScheduleDTO> schedulesDTOs = new ArrayList<>();
        for (Schedule schedule : schedules) {
            schedulesDTOs.add(copyScheduleToDTO(schedule));
        }

        return schedulesDTOs;
    }

    public List<ScheduleDTO> getCustomerSchedule(long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        List<Schedule> schedules = scheduleRepository.getAllByPetsIn(customer.getPets());
        List<ScheduleDTO> schedulesDTOs = new ArrayList<>();
        for (Schedule schedule : schedules) {
            schedulesDTOs.add(copyScheduleToDTO(schedule));
        }

        return schedulesDTOs;
    }

    private Schedule copyScheduleDTOToEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);

        List<Long> employeeIds = new ArrayList<>(scheduleDTO.getEmployeeIds());
        List<Employee> employeesList = new ArrayList<>();
        for (Long employeeId : employeeIds) {
            employeesList.add(employeeService.getEmployee(employeeId));
        }
        schedule.setEmployees(employeesList);

        List<Long> petIdsList = new ArrayList<>(scheduleDTO.getPetIds());
        List<Pet> petsList = new ArrayList<>();

        for (Long petId : petIdsList) {
            petsList.add(petService.getPet(petId));
        }
        // dto saves petIds, entity saves the whole Pet object
        schedule.setPets(petsList);

        // iterate over Set<EmployeeSkill> activities
        Set<EmployeeSkill> activities = new HashSet<>(scheduleDTO.getActivities());
        schedule.setActivities(activities);

        return schedule;
    }

    private ScheduleDTO copyScheduleToDTO(Schedule schedule){
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, dto);

        List<Employee> employeesList = new ArrayList<>(schedule.getEmployees());
        List<Long> employeeIdsList = new ArrayList<>();
        for (Employee employee : employeesList) {
            employeeIdsList.add(employee.getId());
        }
        // dto saves petIds, entity saves the whole Pet object
        dto.setEmployeeIds(employeeIdsList);

        List<Pet> petsList = new ArrayList<>(schedule.getPets());
        List<Long> petIdsList = new ArrayList<>();
        for (Pet pet : petsList) {
            petIdsList.add(pet.getId());
        }
        // dto saves petIds, entity saves the whole Pet object
        dto.setPetIds(petIdsList);


        Set<EmployeeSkill> activities = new HashSet<>(schedule.getActivities());
        dto.setActivities(activities);

        return dto;
    }
}
