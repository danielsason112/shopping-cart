package il.ac.afeka.cloud.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository implements ProductDao {
	private List<String> ids;

	public ProductRepository() {
	}
	
	@PostConstruct
	void init() {
		this.ids = new ArrayList<String>();
		this.ids.add("x45");
		this.ids.add("x78");
	}

	@Override
	public boolean existsById(String productId) {
		return this.ids.contains(productId);
	}

}
