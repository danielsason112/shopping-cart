package il.ac.afeka.cloud.logic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import il.ac.afeka.cloud.layout.CouponBoundary;

@Component
public class ProductsCouponClient {
	private RestTemplate restTemplate;
	private String uri;
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
	}
	
	@Value("${shopping-catalog-service.uri}")
	public void setUri(String uri) {
		this.uri = uri;
	}

	public List<CouponBoundary> getCouponsByProductId(String productId, String sortAttr, String order, int size, int page) {
		return Arrays.asList(
				this.restTemplate.getForObject(this.uri + "/coupons?filterType=byProduct&filterValue={productId}&sortBy={sortAttr}&sortOrder={order}&page={page)&size={size}",
						CouponBoundary[].class,
						productId,
						sortAttr,
						order,
						size,
						page));
	}
		
}
