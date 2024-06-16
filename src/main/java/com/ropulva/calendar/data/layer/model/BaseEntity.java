package com.ropulva.calendar.data.layer.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @CreationTimestamp
    @Column(name = "CREATION_TIME")
    private LocalDateTime creationTime;

    @UpdateTimestamp
    @Column(name = "MODIFICATION_TIME")
    private LocalDateTime updatedAt;
}
