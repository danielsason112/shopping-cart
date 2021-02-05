package il.ac.afeka.cloud.kafka;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import il.ac.afeka.cloud.layout.ShoppingCartBoundary;
import il.ac.afeka.cloud.logic.ShoppingCartService;

@Configuration
public class ShoppingCartConsumer {
	private ShoppingCartService shoppingCartService;

	@Autowired
	public ShoppingCartConsumer(ShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}
	
	@Bean
	public Consumer<ShoppingCartBoundary> receiveAndCreateShoppingCart() {
		return new Consumer<ShoppingCartBoundary>() {

			@Override
			public void accept(ShoppingCartBoundary cart) {
				try {
					shoppingCartService.transactionalCreateShoppingCart(cart);
				} catch (InvalidDataAccessApiUsageException e) {
					shoppingCartService.createShoppingCart(cart);
				}
			}
		};
	}
}
