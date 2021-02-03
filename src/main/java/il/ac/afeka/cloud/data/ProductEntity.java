package il.ac.afeka.cloud.data;

public class ProductEntity {
	private String productId;
	private int amount;
	
	public ProductEntity() {
	}

	public ProductEntity(String productId, int amount) {
		this.productId = productId;
		this.amount = amount;
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

	@Override
	public String toString() {
		return "ProductEntity [productId=" + productId + ", amount=" + amount + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) 
            return true;
		
		if(obj == null || obj.getClass()!= this.getClass()) 
            return false;
		
		return this.getProductId().equals(((ProductEntity) obj).getProductId());
	}
	
	@Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + productId.hashCode();
        return result;
    }
}
