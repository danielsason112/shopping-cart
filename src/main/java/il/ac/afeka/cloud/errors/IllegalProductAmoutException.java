package il.ac.afeka.cloud.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IllegalProductAmoutException extends ResponseStatusException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Illegal product amount.";

	public IllegalProductAmoutException() {
		super(HttpStatus.BAD_REQUEST, MESSAGE);
	}
}
