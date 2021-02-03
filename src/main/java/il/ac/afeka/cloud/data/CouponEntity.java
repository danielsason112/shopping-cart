package il.ac.afeka.cloud.data;

public class CouponEntity {
	private String couponId;

	public CouponEntity() {
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
		return "CouponEntity [couponId=" + couponId + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) 
            return true;
		
		if(obj == null || obj.getClass()!= this.getClass()) 
            return false;
		
		return this.getCouponId() == ((CouponEntity) obj).getCouponId();
	}
	
	
}
