package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
