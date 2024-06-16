package com.ropulva.calendar.business.layer.exception;

import com.ropulva.calendar.business.layer.dtos.BaseDTO;
import com.ropulva.calendar.business.layer.enums.ResponseStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends BaseException {
	private BaseDTO<String> errorObject;

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
		this.errorObject = new BaseDTO<>(ResponseStatusEnum.FAILED.name(),message,false);
	}


}
