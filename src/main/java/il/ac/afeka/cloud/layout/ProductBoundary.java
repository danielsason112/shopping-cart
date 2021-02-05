package il.ac.afeka.cloud.layout;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import il.ac.afeka.cloud.data.ProductEntity;

@JsonInclude(Include.NON_NULL)
public class ProductBoundary {
	private String productId;
	private int amount;
	private Set<CouponBoundary> availableCoupons;

	public ProductBoundary() {
		super();
	}

	public ProductBoundary(String productId, int amount) {
		this.productId = productId;
		this.amount = amount;
	}

	public ProductBoundary(ProductEntity entity) {
		this.productId = entity.getProductId();
		this.amount = entity.getAmount();
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Set<CouponBoundary> getAvailableCoupons() {
		return availableCoupons;
	}

	public void setAvailableCoupons(Set<CouponBoundary> availableCoupons) {
		this.availableCoupons = availableCoupons;
	}
	
	public void addCoupon(CouponBoundary coupon) {
		if (this.availableCoupons == null)
			this.availableCoupons = new HashSet<CouponBoundary>();
		this.availableCoupons.add(coupon);
	}
	
	public void addCoupons(Collection<CouponBoundary> coupons) {
		if (this.availableCoupons == null)
			this.availableCoupons = new HashSet<CouponBoundary>();
		this.availableCoupons.addAll(coupons);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null || obj.getClass() != this.getClass())
			return false;

		return this.getProductId().equals(((ProductBoundary) obj).getProductId());
	}
	
	@Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + productId.hashCode();
        return result;
    }

	@Override
	public String toString() {
		return "ShoppingCartProductBoundary [productId=" + productId + ", amount=" + amount + ", availableCoupons="
				+ availableCoupons + "]";
	}

}
