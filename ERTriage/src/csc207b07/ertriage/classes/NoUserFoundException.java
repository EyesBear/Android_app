package csc207b07.ertriage.classes;

import java.io.Serializable;

public class NoUserFoundException extends Exception implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4722143341421567722L;

	public NoUserFoundException() {
		super();
	}

	public NoUserFoundException(String message) {
		super(message);
	}
}
