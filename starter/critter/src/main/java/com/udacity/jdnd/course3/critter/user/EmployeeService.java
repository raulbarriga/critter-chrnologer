package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // if I use the default crud method (i.e. save(), then I don't have to annotate the method
    // it's already annotated by @Transactional via JpaRepository
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = copyEmployeeDTOToEntity(employeeDTO);
        employee = employeeRepository.save(employee);

        return copyEmployeeToDTO(employee);
    }

    public EmployeeDTO getEmployeeById(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + employeeId + " not found"));
        return copyEmployeeToDTO(employee);
    }

    private Employee copyEmployeeDTOToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        // need to map skills & daysAvailable
        Set<EmployeeSkill> skills = new HashSet<>();
        for (EmployeeSkill skill : employeeDTO.getSkills()) {
            skills.add(skill);
        }
        employee.setSkills(skills);

        Set<DayOfWeek> daysAvailable = new HashSet<>();
        for (DayOfWeek day : employeeDTO.getDaysAvailable()) {
            daysAvailable.add(day);
        }
        employee.setDaysAvailable(daysAvailable);

        return employee;
    }

    private EmployeeDTO copyEmployeeToDTO(Employee employee){
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, dto);

        // need to map skills & daysAvailable
        Set<EmployeeSkill> skills = new HashSet<>();
        for (EmployeeSkill skill : employee.getSkills()) {
            skills.add(skill);
        }
        dto.setSkills(skills);

        Set<DayOfWeek> daysAvailable = new HashSet<>();
        for (DayOfWeek day : employee.getDaysAvailable()) {
            daysAvailable.add(day);
        }
        dto.setDaysAvailable(daysAvailable);

        return dto;
    }
}
