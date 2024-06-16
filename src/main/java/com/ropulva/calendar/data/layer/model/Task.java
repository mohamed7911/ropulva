package com.ropulva.calendar.data.layer.model;

import com.google.api.client.util.DateTime;
import com.ropulva.calendar.business.layer.enums.TaskStatusEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatusEnum status;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime dueTime;
    private boolean isSynced;
}

