package com.example.demo.exception;

public class OrderAlreadyPaidException extends  RuntimeException {
	public OrderAlreadyPaidException(String message) {
		super(message);
	}

}
