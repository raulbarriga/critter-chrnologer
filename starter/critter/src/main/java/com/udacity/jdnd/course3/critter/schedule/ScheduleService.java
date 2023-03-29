package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
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
        List<Schedule> petSchedules = scheduleRepository.findAllByPetId(petId);

        List<ScheduleDTO> petSchedulesDTOs = new ArrayList<>();
        for (Schedule petSchedule : petSchedules) {
            ScheduleDTO scheduleDTO = copyScheduleToDTO(petSchedule);
            petSchedulesDTOs.add(scheduleDTO);
        }

        return petSchedulesDTOs;
    }

    public List<ScheduleDTO> getEmployeeSchedule(long employeeId) {
        List<Schedule> employeeSchedules = scheduleRepository.findAllByEmployeeId(employeeId);

        List<ScheduleDTO> employeeSchedulesDTOs = new ArrayList<>();
        for (Schedule employeeSchedule : employeeSchedules) {
            ScheduleDTO scheduleDTO = copyScheduleToDTO(employeeSchedule);
            employeeSchedulesDTOs.add(scheduleDTO);
        }

        return employeeSchedulesDTOs;
    }

    public List<ScheduleDTO> getCustomerSchedule(long customerId) {
        List<Schedule> customerSchedules = scheduleRepository.findAllByCustomerId(customerId);

        List<ScheduleDTO> customerSchedulesDTOs = new ArrayList<>();
        for (Schedule customerSchedule : customerSchedules) {
            ScheduleDTO scheduleDTO = copyScheduleToDTO(customerSchedule);
            customerSchedulesDTOs.add(scheduleDTO);
        }

        return customerSchedulesDTOs;
    }

    private Schedule copyScheduleDTOToEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        // iterate over List<Long> employeeIds
        // employeeRepository.findById(employeeId)
        //                    .orElseThrow(() -> new EntityNotFoundException("Employee with id " + employeeId + " not found")));
        List<Long> employeeIds = new ArrayList<>(scheduleDTO.getEmployeeIds());
        schedule.setEmployeeIds(employeeIds);

        // iterate over List<Long> employeeIds
        // petRepository.findById(petId)
        //                    .orElseThrow(() -> new EntityNotFoundException("Pet with id " + petId + " not found")))
        List<Long> petIds = new ArrayList<>(scheduleDTO.getPetIds());
        schedule.setPetIds(petIds);

        // iterate over Set<EmployeeSkill> activities
        Set<EmployeeSkill> activities = new HashSet<>(scheduleDTO.getActivities());
        schedule.setActivities(activities);

        return schedule;
    }

    private ScheduleDTO copyScheduleToDTO(Schedule schedule){
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, dto);

        List<Long> employeeIds = new ArrayList<>(schedule.getEmployeeIds());
        dto.setEmployeeIds(employeeIds);

        List<Long> petIds = new ArrayList<>(schedule.getPetIds());
        dto.setPetIds(petIds);

        Set<EmployeeSkill> activities = new HashSet<>(schedule.getActivities());
        dto.setActivities(activities);

        return dto;
    }
}
