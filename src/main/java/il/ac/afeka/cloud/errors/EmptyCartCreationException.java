package il.ac.afeka.cloud.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmptyCartCreationException  extends ResponseStatusException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Can not creat an empty cart with no products.";

	public EmptyCartCreationException() {
		super(HttpStatus.BAD_REQUEST, MESSAGE);
	}
}