package com.cboxgames.idonia.backend.commons;

public class UnimplementedEnumException extends RuntimeException {
	
	public UnimplementedEnumException() {
    }
	
	public UnimplementedEnumException(String msg) {
        super(msg);
    }
}
