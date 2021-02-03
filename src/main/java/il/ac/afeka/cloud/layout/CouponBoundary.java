package il.ac.afeka.cloud.layout;

public class CouponBoundary {
	private String couponId;

	public CouponBoundary() {
		super();
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	@Override
	public String toString() {
		return "ShoppingCartCouponBoundary [couponId=" + couponId + "]";
	}
	
	
}
