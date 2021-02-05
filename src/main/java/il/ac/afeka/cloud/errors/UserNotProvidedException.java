package il.ac.afeka.cloud.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotProvidedException extends ResponseStatusException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "User not provided.";

	public UserNotProvidedException() {
		super(HttpStatus.BAD_REQUEST, MESSAGE);
	}
}
