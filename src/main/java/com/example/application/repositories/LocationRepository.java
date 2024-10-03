package com.example.application.repositories;

import com.example.application.views.list.Location;
import com.example.application.views.list.Schedule;
import com.example.application.views.list.UserForm;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocationRepository extends CrudRepository<Location, Long> {
    List<Location> findByUser(UserForm user);
}
