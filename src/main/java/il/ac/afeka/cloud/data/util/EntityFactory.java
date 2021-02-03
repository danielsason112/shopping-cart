package il.ac.afeka.cloud.data.util;

import java.util.Map;
import java.util.Set;

import il.ac.afeka.cloud.data.ProductEntity;
import il.ac.afeka.cloud.data.ShoppingCartEntity;
import il.ac.afeka.cloud.data.UserEntity;

public interface EntityFactory {

	ShoppingCartEntity createNewShoppingCart(UserEntity userEntity, Set<ProductEntity> products, Map<String, Object> moreProperties);

}
