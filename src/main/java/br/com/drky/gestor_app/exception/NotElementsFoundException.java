package br.com.drky.gestor_app.exception;

public class NotElementsFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotElementsFoundException(String msg) {
		super(msg);
	}
}