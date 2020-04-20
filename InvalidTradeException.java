package com.tradestore.main;

public class InvalidTradeException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidTradeException(String s) {
		super(s);
	}

	public InvalidTradeException(String string, Exception e) {
		super(string, e);
	}
}
