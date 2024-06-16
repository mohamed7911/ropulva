package com.ropulva.calendar.business.layer.dtos;


import com.google.api.client.util.DateTime;
import com.ropulva.calendar.business.layer.enums.TaskStatusEnum;
import com.ropulva.calendar.data.layer.model.Task;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class TaskDTO implements Serializable {
    private Long taskId;
    private String username;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime dueTime;
    private TaskStatusEnum status;
    private String notes;


    public TaskDTO (Task task) {
        this.taskId = task.getId();
        this.username= task.getUser().getUsername();
        this.title= task.getTitle();
        this.startTime= task.getStartedAt();
        this.dueTime= task.getDueTime();
        this.status= task.getStatus();
        this.notes= task.getNotes();
    }

}
