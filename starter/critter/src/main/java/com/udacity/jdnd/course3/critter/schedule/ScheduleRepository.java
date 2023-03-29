package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByPetId(long petId);
    List<Schedule> findAllByEmployeeId(long employeeId);
    List<Schedule> findAllByCustomerId(long customerId);
}
