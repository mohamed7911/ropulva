package com.ropulva.calendar.business.layer.services.Impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;
import com.ropulva.calendar.business.layer.dtos.BaseDTO;
import com.ropulva.calendar.business.layer.dtos.TaskDTO;
import com.ropulva.calendar.business.layer.enums.ResponseStatusEnum;
import com.ropulva.calendar.business.layer.services.TaskService;
import com.ropulva.calendar.business.layer.common.Utility;
import com.ropulva.calendar.business.layer.enums.TaskStatusEnum;
import com.ropulva.calendar.business.layer.exception.BusinessException;
import com.ropulva.calendar.business.layer.services.UserService;
import com.ropulva.calendar.data.layer.model.Task;
import com.ropulva.calendar.data.layer.model.User;
import com.ropulva.calendar.data.layer.repository.TaskRepository;
import com.ropulva.calendar.data.layer.repository.UserRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.StandardServletAsyncWebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.ropulva.calendar.business.layer.services.Impl.TasksQuickstart.JSON_FACTORY;
import static com.ropulva.calendar.business.layer.services.Impl.TasksQuickstart.getCredentials;

@Service
@CacheConfig(cacheNames = "task")
@CommonsLog
public class TaskServiceImpl implements TaskService {
    private static final String APPLICATION_NAME = "Google Tasks API Java Quickstart";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    @Override
    @CacheEvict(key = "#taskDTO.username")
    public BaseDTO<String> addTask(TaskDTO taskDTO) throws BusinessException {
        try {
            log.debug("entering addTask func");
            Task task = new Task();
            User user = userService.validateUser(taskDTO.getUsername());

            if (!user.isVerified()){
                throw new BusinessException("User is not verified, please verify your account");
            }
            if (Utility.isNetworkAvailable()) {
                    addTaskToCloud(user.getUsername(),taskDTO);
                    task.setSynced(true);
            } else {
                task.setSynced(false);
            }
            task.setUser(user);
            task.setTitle(taskDTO.getTitle());
            task.setStatus(TaskStatusEnum.IN_PROGRESS);
            task.setStartedAt(LocalDateTime.now());
            task.setNotes(taskDTO.getNotes());
            task.setDueTime(taskDTO.getDueTime());

            taskRepository.save(task);
            log.debug("Success addTask func");
            return new BaseDTO<>(ResponseStatusEnum.SUCCESS.name(), "Task added Successfully",true);

        }catch (BusinessException e){
        throw e;
        }
        catch (Exception e){
        throw new BusinessException("General Error");
        }
    }

    @Override
    public boolean addTaskToCloud(String username, TaskDTO taskDTO) throws BusinessException {
            try {
                log.debug("entering addTaskToCloud func");

                final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                Tasks service = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, username))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

                com.google.api.services.tasks.model.Task task = new com.google.api.services.tasks.model.Task()
                        .setTitle(taskDTO.getTitle())
                        .setNotes(taskDTO.getNotes())
                        .setDue(new DateTime(String.valueOf(taskDTO.getDueTime())))
                        .setStatus(TaskStatusEnum.IN_PROGRESS.name());

                TaskList taskList = new TaskList();
                taskList.setTitle("New Task");

                TaskList result = service.tasklists().insert(taskList).execute();

                service.tasks().insert(result.getId(), task).execute();
                log.debug("Success addTaskToCloud func");

                return true;
            }
            catch (Exception e){
                throw new BusinessException("General Error");
            }
    }

    @Override
    @Transactional
    @CacheEvict(key = "#taskDTO.username")
    public BaseDTO<Task> updateTask(TaskDTO taskDTO) throws BusinessException {

        try {
            log.debug("entering updateTask func");

            Task task = validateTask(taskDTO);

            task.setTitle(taskDTO.getTitle());
            task.setNotes(taskDTO.getNotes());
            task.setStatus(taskDTO.getStatus());
            task.setDueTime(taskDTO.getDueTime());

            if (taskDTO.getStatus() == TaskStatusEnum.COMPLETED) {
                task.setCompletedAt(LocalDateTime.now());
            }

            taskRepository.save(task);

            BaseDTO<Task> baseDTO = new BaseDTO<>(ResponseStatusEnum.SUCCESS.name(), task,true);
            log.debug("Success updateTask func");

            return baseDTO;
        }catch (BusinessException e){
            throw e;
        }
        catch (Exception e){
            throw new BusinessException("General Error");
        }

    }

    @Override
    @CacheEvict(key = "#taskDTO.username")
    public BaseDTO<String> deleteTask(TaskDTO taskDTO) throws BusinessException {
        try {
            log.debug("entering deleteTask func");

            Task task = validateTask(taskDTO);

            taskRepository.delete(task);

            BaseDTO<String> baseDTO = new BaseDTO<>(ResponseStatusEnum.SUCCESS.name(), TaskStatusEnum.DELETED.name(),true);
            log.debug("Success deleteTask func");

            return baseDTO;
        }catch (BusinessException e){
            throw e;
        }
        catch (Exception e){
            throw new BusinessException("General Error");
        }
    }

    @Override
    @Cacheable(key = "#username")
    public BaseDTO<List<TaskDTO>> getTasks(String username) throws BusinessException {
        try {
            log.debug("entering getTasks func");

            User user = userService.validateUser(username);

            List<Task> tasks = taskRepository.findAllByUserId(user.getId());

            List<TaskDTO> taskDTOs = tasks.stream()
                    .map(TaskDTO::new)
                    .collect(Collectors.toList());

            BaseDTO<List<TaskDTO>> baseDTO = new BaseDTO<>(ResponseStatusEnum.SUCCESS.name(), taskDTOs,true);
            log.debug("Success getTasks func");

            return baseDTO;
        }catch (BusinessException e){
            throw e;
        }
        catch (Exception e){
            throw new BusinessException("General Error");
        }
    }

    @Override
    public Task validateTask(TaskDTO taskDTO) throws BusinessException {
        log.debug("entering validateTask func");

        User user = userService.validateUser(taskDTO.getUsername());

        Task task = taskRepository.findById(taskDTO.getTaskId())
                .orElseThrow(() -> new BusinessException("Task doesn't exist"));

        if (!task.getUser().equals(user)) {
            throw new BusinessException("Task does not belong to the user");
        }
        log.debug("Success validateTask func");

        return task;
    }

    @Override
    public BaseDTO<String> syncTasks(String username) throws BusinessException {
        try {
            User user = userService.validateUser(username);

            if (!user.isVerified()){
                throw new BusinessException("User is not verified, please verify your account");
            }

            List<Task> tasks = taskRepository.findAllByUserIdAndIsSyncedFalse(user.getId());

            if (tasks.size() == 0){
                return  new BaseDTO<>(ResponseStatusEnum.SUCCESS.name(), "no unSynced task found",true);
            }

            if (Utility.isNetworkAvailable()) {
                for (Task task : tasks){
                    TaskDTO taskDTO = new TaskDTO(task);
                    boolean isSynced = addTaskToCloud(user.getUsername(), taskDTO);
                    if (isSynced) {
                        task.setSynced(true);
                        taskRepository.save(task);
                    }
                }
            } else {
                throw new BusinessException("no Internet Connection");
            }

            return  new BaseDTO<>(ResponseStatusEnum.SUCCESS.name(), "Sync Success",true);
        }catch (BusinessException e){
            throw e;
        }
        catch (Exception e){
            throw new BusinessException("General Error");
        }
    }


}
