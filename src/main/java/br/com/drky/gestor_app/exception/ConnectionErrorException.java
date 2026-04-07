package br.com.drky.gestor_app.exception;

public class ConnectionErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConnectionErrorException(String msg) {
		super(msg);
	}
}