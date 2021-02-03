package il.ac.afeka.cloud.logic;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import il.ac.afeka.cloud.dao.ProductDao;
import il.ac.afeka.cloud.dao.ShoppingCartDao;
import il.ac.afeka.cloud.dao.UserDao;
import il.ac.afeka.cloud.data.ProductEntity;
import il.ac.afeka.cloud.data.ShoppingCartEntity;
import il.ac.afeka.cloud.data.UserEntity;
import il.ac.afeka.cloud.data.util.EntityFactory;
import il.ac.afeka.cloud.enums.FilterTypeEnum;
import il.ac.afeka.cloud.enums.SortAttrEnum;
import il.ac.afeka.cloud.enums.SortOrderEnum;
import il.ac.afeka.cloud.layout.ProductBoundary;
import il.ac.afeka.cloud.layout.ShoppingCartBoundary;
import il.ac.afeka.cloud.layout.UserBoundary;

@Service
public class ShoppingCartServiceImp implements ShoppingCartService {
	private ShoppingCartDao shoppingCartDao;
	private UserDao userDao;
	private ProductDao productDao;
	private EntityFactory entityFactory;
	
	@Autowired
	public ShoppingCartServiceImp(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao,
			EntityFactory entityFactory) {
		this.shoppingCartDao = shoppingCartDao;
		this.userDao = userDao;
		this.productDao = productDao;
		this.entityFactory = entityFactory;
	}

	@Override
	@Transactional
	public ShoppingCartBoundary createShoppingCart(ShoppingCartBoundary shoppingCartBoundary) {
		UserBoundary userBoundary = shoppingCartBoundary.getUser();
		
		if (userBoundary == null)
			throw new RuntimeException("Can not create shopping cart, user must be provided in the request."); // TODO Throw error code 400
		
		if (!this.userDao.existsById(userBoundary.getEmail()))
			throw new RuntimeException("Can not create shopping cart, no such user exists."); // TODO return error code 400
			
		Set<ProductBoundary> products = shoppingCartBoundary.getProducts();
		
		if (products != null)
			products.forEach(product -> {
				if (!this.productDao.existsById(product.getProductId()))
					throw new RuntimeException("Can not create shopping cart, one of the products does not exists."); // TODO return error code 400
				if (product.getAmount() <= 0)
					throw new RuntimeException("Can not create shopping cart with a product amount smaller than 1."); // TODO return error code 400
			});
		
		UserEntity userEntity = new UserEntity(userBoundary.getEmail());
		
		List<ShoppingCartEntity> notExpiredCart = this.shoppingCartDao.findByUserAndExpired(userEntity, false);
		
		notExpiredCart.forEach(cart -> {
			cart.setExpired(true);
			this.shoppingCartDao.save(cart);
		});
		
		return this.toShoppingCartBoundary(
				this.shoppingCartDao.save(
				this.entityFactory.createNewShoppingCart(
						userEntity,
						products.stream()
							.map(p -> new ProductEntity(
								p.getProductId(), p.getAmount()))
							.collect(Collectors.toSet()),
						shoppingCartBoundary.getMoreProperties())));
	}

	@Override
	public ShoppingCartBoundary getShoppingCart(String shoppingCartId) {
		ShoppingCartEntity entity = this.shoppingCartDao.findById(shoppingCartId).orElse(null);
		
		if (entity == null)
			throw new RuntimeException("No such shopping cart exists."); // TODO return error code 404
		
		return this.toShoppingCartBoundary(entity);
	}

	@Override
	public ShoppingCartBoundary getShoppingCartByEmail(String email) {
		List<ShoppingCartEntity> notExpiredCarts = this.shoppingCartDao.findByUserAndExpired(new UserEntity(email), false);
		
		if (notExpiredCarts.isEmpty())
			throw new RuntimeException("No current cart for user exists."); // TODO return error code 404
		
		return this.toShoppingCartBoundary(notExpiredCarts.get(0));
	}

	@Override
	@Transactional
	public void updateShoppingCart(String email, ShoppingCartBoundary shoppingCartBoundary) {

		List<ShoppingCartEntity> notExpiredCarts = this.shoppingCartDao.findByUserAndExpired(new UserEntity(email), false);
		
		if (notExpiredCarts.isEmpty())
			throw new RuntimeException("No current cart for user exists."); // TODO return error code 404
		
		ShoppingCartEntity entity = notExpiredCarts.get(0);
				
		if (!entity.isExpired())
			entity.setExpired(shoppingCartBoundary.isExpired());
		
		Map<String, Object> entityMP = entity.getMoreProperties();
		Map<String, Object> boundaryMP = shoppingCartBoundary.getMoreProperties();
		
		if (boundaryMP != null && entityMP != null)
			entityMP.putAll(boundaryMP);
		else if (boundaryMP != null)
			entity.setMoreProperties(boundaryMP);
		
		Set<ProductBoundary> boundaryProducts = shoppingCartBoundary.getProducts();
		Set<ProductEntity> entityProducts = entity.getProducts();
		
		if (boundaryProducts != null)
			boundaryProducts.forEach(p -> {
				if (!this.productDao.existsById(p.getProductId()))
					throw new RuntimeException("Can not create shopping cart, one of the products does not exists."); // TODO return error code 400
				if (p.getAmount() < 0)
					throw new RuntimeException("Can not update shopping cart with a negative product amount."); // TODO return error code 400
				
				ProductEntity pe = new ProductEntity(p.getProductId(), p.getAmount());
				
				entityProducts.remove(pe);
				if (pe.getAmount() != 0)
					entityProducts.add(pe);
			});
		this.shoppingCartDao.save(entity);
	}

	@Override
	public ShoppingCartBoundary[] getAll(FilterTypeEnum filterType, String filterValue, SortAttrEnum sortBy,
			SortOrderEnum sortOrder, int size, int page) {
		
		if (filterType == FilterTypeEnum.byNotExpired)
			return this.shoppingCartDao.findByExpired(false)
					.stream()
					.map(this::toShoppingCartBoundary)
					.collect(Collectors.toList())
					.toArray(new ShoppingCartBoundary[0]);
		
		if (filterType == FilterTypeEnum.byUserEmail && !filterValue.isBlank())
			return this.shoppingCartDao.findByUser(new UserEntity(filterValue))
					.stream()
					.map(this::toShoppingCartBoundary)
					.collect(Collectors.toList())
					.toArray(new ShoppingCartBoundary[0]);
			
		return StreamSupport.stream(
				this.shoppingCartDao
				.findAll(PageRequest.of(page,
						size,
						sortOrder == SortOrderEnum.ASC ? Direction.ASC : Direction.DESC,
						sortBy == SortAttrEnum.creation ? "creationTimestamp" : "user", "shoppingCartId"))
				.spliterator(), false)
					.map(this::toShoppingCartBoundary)
					.collect(Collectors.toList())
					.toArray(new ShoppingCartBoundary[0]);
	}

	private ShoppingCartBoundary toShoppingCartBoundary(ShoppingCartEntity entity) {
		ShoppingCartBoundary boundary = new ShoppingCartBoundary();
		boundary.setUser(new UserBoundary(entity.getUser().getEmail()));
		boundary.setShoppingCartId(entity.getShoppingCartId());
		boundary.setCreationTimestamp(entity.getCreationTimestamp());
		boundary.setProducts(
				new HashSet<ProductBoundary>(
						entity.getProducts()
						.stream()
						.map(ProductBoundary::new)
						.collect(Collectors.toList())));
		boundary.setExpired(entity.isExpired());
		boundary.setMoreProperties(entity.getMoreProperties());
		
		return boundary;
	}
	
}
