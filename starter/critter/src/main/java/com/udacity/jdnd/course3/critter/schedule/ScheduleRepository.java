package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Transactional
    List<Schedule> getAllByPetsContains(Pet pet);

    @Transactional
    List<Schedule> getAllByEmployeesContains(Employee employee);

    @Transactional
    List<Schedule> getAllByPetsIn(List<Pet> pets);
}
