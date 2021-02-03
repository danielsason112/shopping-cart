package il.ac.afeka.cloud.layout;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class ShoppingCartBoundary {
	private String shoppingCartId;
	private UserBoundary user;
	private Date creationTimestamp;
	private Set<ProductBoundary> products;
	private boolean expired;
	private Map<String, Object> moreProperties;

	public ShoppingCartBoundary() {
		super();
	}

	public String getShoppingCartId() {
		return shoppingCartId;
	}

	public void setShoppingCartId(String shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}

	public UserBoundary getUser() {
		return user;
	}

	public void setUser(UserBoundary user) {
		this.user = user;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Set<ProductBoundary> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductBoundary> products) {
		this.products = products;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	@JsonAnyGetter
	public Map<String, Object> getMoreProperties() {
		return moreProperties;
	}
	
	@JsonAnySetter
	public void add(String key, Object value) {
		if (this.moreProperties == null) this.moreProperties = new HashMap<String, Object>();
		this.moreProperties.put(key, value);
	}

	public void setMoreProperties(Map<String, Object> moreProperties) {
		this.moreProperties = moreProperties;
	}

	@Override
	public String toString() {
		return "ShoppingCartBoundary [shoppingCartId=" + shoppingCartId + ", user=" + user + ", creationTimestamp="
				+ creationTimestamp + ", products=" + products + ", expired=" + expired + ", moreProperties="
				+ moreProperties + "]";
	}

}
