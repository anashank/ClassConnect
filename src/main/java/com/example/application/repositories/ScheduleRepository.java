package com.example.application.repositories;

import com.example.application.views.list.Schedule;
import com.example.application.views.list.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByProfile(Profile profile);
}
