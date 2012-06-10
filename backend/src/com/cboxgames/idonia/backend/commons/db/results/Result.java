package com.cboxgames.idonia.backend.commons.db.results;

public class Result {

	private boolean success;
	private Object data;
	private String reason;
	
	public Result(String reason) {
		this.reason = reason;
		success = false;
	}
	
	public Result(Object data) {
		success = true;
		this.data = data;
	}
	
	public Result() {
	}
}
