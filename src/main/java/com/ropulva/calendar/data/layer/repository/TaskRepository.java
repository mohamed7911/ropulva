package com.ropulva.calendar.data.layer.repository;

import com.ropulva.calendar.data.layer.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findAllByUserId(long userId);

    List<Task> findAllByUserIdAndIsSyncedFalse(Long userId);
}
