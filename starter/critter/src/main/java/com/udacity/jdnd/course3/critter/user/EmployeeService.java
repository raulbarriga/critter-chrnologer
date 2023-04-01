package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
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

    public void setEmployeeAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + employeeId + " not found"));

        // not returning a dto since we're updating a PUT request
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    public List<EmployeeDTO> getEmployeesAvailability(LocalDate givenDate, Set<EmployeeSkill> givenSkills) {
        // employeeRequestDTO has both LocalDate date & Set<EmployeeSkill> skills
        DayOfWeek givenDayOfWeek = givenDate.getDayOfWeek();
        List<EmployeeDTO> employeeDTOMatches = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            boolean isAvailableOnGivenDay = employee.getDaysAvailable().contains(givenDayOfWeek);
            if (isAvailableOnGivenDay) {
                if (isAvailableOnGivenDay && employee.getSkills().containsAll(givenSkills)) {
                    employeeDTOMatches.add(copyEmployeeToDTO(employee));
                }
            }
        }

        return employeeDTOMatches;
    }

    public Employee getEmployee(long employeeId){
        return employeeRepository.getOne(employeeId);
    }

    private Employee copyEmployeeDTOToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        // need to map skills & daysAvailable
        Set<EmployeeSkill> skills = new HashSet<>(employeeDTO.getSkills());
        employee.setSkills(skills);

        Set<DayOfWeek> daysAvailable = employeeDTO.getDaysAvailable();
        if (daysAvailable != null && !daysAvailable.isEmpty()) {
            employee.setDaysAvailable(daysAvailable);
        }

        return employee;
    }

    private EmployeeDTO copyEmployeeToDTO(Employee employee){
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, dto);

        // need to map skills & daysAvailable
        Set<EmployeeSkill> skills = new HashSet<>(employee.getSkills());
        dto.setSkills(skills);

        Set<DayOfWeek> daysAvailable = employee.getDaysAvailable();
        if (daysAvailable != null && !daysAvailable.isEmpty()) {
            dto.setDaysAvailable(daysAvailable);
        }

        return dto;
    }
}
