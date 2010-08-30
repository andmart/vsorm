package br.com.toolbox.simpleorm.exception;

public class SimpleOrmException extends Exception{

	public SimpleOrmException() {
		super();
	}
	
	public SimpleOrmException(String msg) {
		super(msg);
	}
	
	public SimpleOrmException(Exception e) {
		super(e);
		this.setStackTrace(e.getStackTrace());
	}
}
