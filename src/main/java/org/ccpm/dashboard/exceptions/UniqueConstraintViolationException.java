package org.ccpm.dashboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UniqueConstraintViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;

	public UniqueConstraintViolationException(String message) {
		super(message);
	}

	public UniqueConstraintViolationException(String message, int code) {
		super(message);
		setCode(code);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
