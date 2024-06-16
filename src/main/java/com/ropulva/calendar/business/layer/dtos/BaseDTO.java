package com.ropulva.calendar.business.layer.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDTO<T> implements Serializable {
    private String status;
    private T data;
    private boolean isSuccess;
}
