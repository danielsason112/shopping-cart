package il.ac.afeka.cloud.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import il.ac.afeka.cloud.data.ShoppingCartEntity;
import il.ac.afeka.cloud.data.UserEntity;

public interface ShoppingCartDao extends PagingAndSortingRepository<ShoppingCartEntity, String> {

	public List<ShoppingCartEntity> findByUserAndExpired(UserEntity userEntity, boolean b);

	public List<ShoppingCartEntity> findByExpired(boolean b);

	public List<ShoppingCartEntity> findByUser(UserEntity userEntity);
	
}
