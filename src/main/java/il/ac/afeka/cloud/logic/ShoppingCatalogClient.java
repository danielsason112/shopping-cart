package il.ac.afeka.cloud.logic;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import il.ac.afeka.cloud.layout.shoppingCatalog.Product;

@Component
public class ShoppingCatalogClient {
	private RestTemplate restTemplate;
	private String uri;
	
	
	public ShoppingCatalogClient() {
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
	}
	
	@Value("${shopping-catalog-service.uri}")
	public void setUri(String uri) {
		this.uri = uri;
	}

	public Product getProductById(String productId) {
		return this.restTemplate.getForObject(this.uri + "/shopping/products/{productId}", Product.class, productId);
	}

}
