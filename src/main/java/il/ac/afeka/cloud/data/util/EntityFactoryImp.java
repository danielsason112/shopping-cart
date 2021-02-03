package il.ac.afeka.cloud.data.util;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import il.ac.afeka.cloud.data.ProductEntity;
import il.ac.afeka.cloud.data.ShoppingCartEntity;
import il.ac.afeka.cloud.data.UserEntity;

@Component
public class EntityFactoryImp implements EntityFactory {

	public EntityFactoryImp() {
	}

	@Override
	public ShoppingCartEntity createNewShoppingCart(UserEntity userEntity, Set<ProductEntity> products, Map<String, Object> moreProperties) {
		ShoppingCartEntity entity = new ShoppingCartEntity();
		entity.setUser(userEntity);
		entity.setCreationTimestamp(new Date());
		entity.setExpired(false);
		entity.setProducts(products);
		entity.setMoreProperties(moreProperties);
		return entity;
	}

}
