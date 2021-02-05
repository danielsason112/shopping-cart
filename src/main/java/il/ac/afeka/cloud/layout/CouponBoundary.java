package il.ac.afeka.cloud.layout;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CouponBoundary {
	private String couponId;
	private boolean isUsed;

	public CouponBoundary() {
	}

	public CouponBoundary(String couponId, boolean isUsed) {
		this.couponId = couponId;
		this.isUsed = isUsed;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	@JsonIgnore
	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	@Override
	public String toString() {
		return "ShoppingCartCouponBoundary [couponId=" + couponId + "]";
	}
	
	
}
