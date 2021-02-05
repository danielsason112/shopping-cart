package il.ac.afeka.cloud.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ShoppingCartNotFoundException extends ResponseStatusException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "Shopping cart not found.";

	public ShoppingCartNotFoundException() {
		super(HttpStatus.NOT_FOUND, MESSAGE);
	}
}
