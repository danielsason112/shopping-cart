package il.ac.afeka.cloud.data;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carts")
public class ShoppingCartEntity {
	private String shoppingCartId;
	private UserEntity user;
	private Date creationTimestamp;
	private Set<ProductEntity> products;
	private boolean expired;
	private Map<String, Object> moreProperties;

	public ShoppingCartEntity() {
	}

	@Id
	public String getShoppingCartId() {
		return shoppingCartId;
	}

	public void setShoppingCartId(String shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Set<ProductEntity> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductEntity> products) {
		this.products = products;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public Map<String, Object> getMoreProperties() {
		return moreProperties;
	}

	public void setMoreProperties(Map<String, Object> moreProperties) {
		this.moreProperties = moreProperties;
	}
	
	public void addProduct(ProductEntity product) {
		if (this.products == null)
			this.products = new HashSet<ProductEntity>();
		this.products.add(product);
	}
	
	public void removeProduct(ProductEntity product) {
		if (this.products != null)
			this.products.remove(product);
	}

	public void addMoreProperties(String key, Object value) {
		if (this.moreProperties == null)
			this.moreProperties = new HashMap<String, Object>();
		this.moreProperties.put(key, value);
	}

	public void addMoreProperties(Map<String, Object> map) {
		if (this.moreProperties == null)
			this.moreProperties = new HashMap<String, Object>();
		this.moreProperties.putAll(map);
	}

	@Override
	public String toString() {
		return "ShoppingCartEntity [shoppingCartId=" + shoppingCartId + ", user=" + user + ", creationTimestamp="
				+ creationTimestamp + ", products=" + products + ", expired=" + expired + ", moreProperties="
				+ moreProperties + "]";
	}

}
