package il.ac.afeka.cloud.dao;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import il.ac.afeka.cloud.data.ShoppingCartEntity;
import il.ac.afeka.cloud.data.UserEntity;

public interface ShoppingCartDao extends PagingAndSortingRepository<ShoppingCartEntity, String> {

	public List<ShoppingCartEntity> findByUserAndExpired(UserEntity userEntity, boolean b);

	public List<ShoppingCartEntity> findByExpired(boolean b, PageRequest pageRequest);

	public List<ShoppingCartEntity> findByUser(UserEntity userEntity, PageRequest pageRequest);
	
}
