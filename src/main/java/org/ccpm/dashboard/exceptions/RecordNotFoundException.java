package org.ccpm.dashboard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int code;
	private int id;

	public RecordNotFoundException(String message) {
		super(message);
	}

	public RecordNotFoundException(String message, int code) {
		super(message);
		setCode(code);
	}

	public RecordNotFoundException(String message, int code, int id) {
		super(message);
		setCode(code);
		setId(id);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}