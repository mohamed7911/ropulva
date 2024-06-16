package com.ropulva.calendar.business.layer.services;

import com.ropulva.calendar.business.layer.dtos.BaseDTO;
import com.ropulva.calendar.business.layer.dtos.TaskDTO;
import com.ropulva.calendar.business.layer.exception.BusinessException;
import com.ropulva.calendar.data.layer.model.Task;

import java.util.List;

public interface TaskService {

    BaseDTO<String> addTask(TaskDTO taskDTO) throws BusinessException;

    boolean addTaskToCloud(String username, TaskDTO taskDTO) throws BusinessException;

    BaseDTO<Task> updateTask(TaskDTO taskDTO) throws BusinessException;

    BaseDTO<String> deleteTask(TaskDTO taskDTO) throws BusinessException;

    BaseDTO<List<TaskDTO>> getTasks(String username) throws BusinessException;

    Task validateTask(TaskDTO taskDTO) throws BusinessException;

    BaseDTO<String> syncTasks(String username) throws BusinessException;
}
