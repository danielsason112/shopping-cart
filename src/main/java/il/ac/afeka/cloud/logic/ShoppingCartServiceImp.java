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
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import il.ac.afeka.cloud.dao.ShoppingCartDao;
import il.ac.afeka.cloud.data.ProductEntity;
import il.ac.afeka.cloud.data.ShoppingCartEntity;
import il.ac.afeka.cloud.data.UserEntity;
import il.ac.afeka.cloud.data.util.EntityFactory;
import il.ac.afeka.cloud.enums.FilterTypeEnum;
import il.ac.afeka.cloud.enums.SortAttrEnum;
import il.ac.afeka.cloud.enums.SortOrderEnum;
import il.ac.afeka.cloud.errors.EmptyCartCreationException;
import il.ac.afeka.cloud.errors.IllegalProductAmoutException;
import il.ac.afeka.cloud.errors.IllegalProductException;
import il.ac.afeka.cloud.errors.ProductNotFoundException;
import il.ac.afeka.cloud.errors.ShoppingCartNotFoundException;
import il.ac.afeka.cloud.errors.UserNotFoundException;
import il.ac.afeka.cloud.errors.UserNotProvidedException;
import il.ac.afeka.cloud.layout.CouponBoundary;
import il.ac.afeka.cloud.layout.ProductBoundary;
import il.ac.afeka.cloud.layout.ShoppingCartBoundary;
import il.ac.afeka.cloud.layout.UserBoundary;

@Service
public class ShoppingCartServiceImp implements ShoppingCartService {
	private final int COUPONS_REQUEST_SIZE = 100;
	private final int MAX_COUPONS_REQUEST_SIZE = 1000;
	private ShoppingCartDao shoppingCartDao;
	private EntityFactory entityFactory;
	private UsersManagementClient usersManagementClient;
	private ShoppingCatalogClient shoppingCatalogClient;
	private ProductsCouponClient productsCouponClient;
	
	@Autowired
	public ShoppingCartServiceImp(ShoppingCartDao shoppingCartDao, EntityFactory entityFactory,
			UsersManagementClient usersManagementClient, ShoppingCatalogClient shoppingCatalogClient, ProductsCouponClient productsCouponClient) {
		this.shoppingCartDao = shoppingCartDao;
		this.entityFactory = entityFactory;
		this.usersManagementClient = usersManagementClient;
		this.shoppingCatalogClient = shoppingCatalogClient;
		this.productsCouponClient = productsCouponClient;
	}

	@Override
	public ShoppingCartBoundary createShoppingCart(ShoppingCartBoundary shoppingCartBoundary) {
		UserBoundary userBoundary = shoppingCartBoundary.getUser();
		
		if (userBoundary == null)
			throw new UserNotProvidedException();
		
		if (userBoundary.getEmail() == null || userBoundary.getEmail().isBlank())
			throw new UserNotProvidedException();
		
		if (!this.UserExistsByEmail(userBoundary.getEmail()))
			throw new UserNotFoundException();
			
		Set<ProductBoundary> products = shoppingCartBoundary.getProducts();
		
		if (products == null)
			throw new EmptyCartCreationException();
		
		if (products.isEmpty())
			throw new EmptyCartCreationException();
		
		products.forEach(p -> this.validateProductBoundary(p));
		
		UserEntity userEntity = new UserEntity(userBoundary.getEmail());
		
		this.shoppingCartDao.findByUserAndExpired(userEntity, false)
			.forEach(cart -> {
				cart.setExpired(true);
				this.shoppingCartDao.save(cart);
			});
		
		return this.toShoppingCartBoundary(
				this.shoppingCartDao.save(
				this.entityFactory.createNewShoppingCart(
						userEntity,
						products.stream()
							.map(this::toProductEntity)
							.collect(Collectors.toSet()),
						shoppingCartBoundary.getMoreProperties())));
	}
	
	@Override
	@Transactional
	public ShoppingCartBoundary transactionalCreateShoppingCart(ShoppingCartBoundary shoppingCartBoundary) {
		return this.createShoppingCart(shoppingCartBoundary);
	}

	@Override
	public ShoppingCartBoundary getShoppingCart(String shoppingCartId) {
		ShoppingCartEntity entity = this.shoppingCartDao.findById(shoppingCartId).orElse(null);
		
		if (entity != null)
			return this.toShoppingCartBoundary(entity);
		
		return this.getShoppingCartByEmail(shoppingCartId);
	}

	@Override
	public ShoppingCartBoundary getShoppingCartByEmail(String email) {
		List<ShoppingCartEntity> notExpiredCarts = this.shoppingCartDao.findByUserAndExpired(new UserEntity(email), false);
		
		if (notExpiredCarts.isEmpty())
			throw new ShoppingCartNotFoundException();
		
		ShoppingCartBoundary shoppingCart = this.toShoppingCartBoundary(notExpiredCarts.get(0));
		
		
		
		shoppingCart.getProducts().forEach(p -> {
			List<CouponBoundary> coupons;
			int page = 0;
			int size = COUPONS_REQUEST_SIZE;
			try {
				do {
					coupons = this.productsCouponClient.getCouponsByProductId(p.getProductId(), "isUsed", "ASC", size, page);
					p.addCoupons(
							coupons.stream()
							.takeWhile(c -> !c.isUsed())
									.collect(Collectors.toSet()));
					
					if (!coupons.isEmpty())
						if(coupons.get(coupons.size() - 1).isUsed())
							break;
					page++;
				} while (coupons.size() == size || page * COUPONS_REQUEST_SIZE > MAX_COUPONS_REQUEST_SIZE);
			} catch (Exception e) {
				System.err.println(e);
			}
			
		});
				
		return shoppingCart;
	}

	@Override
	public void updateShoppingCart(String email, ShoppingCartBoundary shoppingCartBoundary) {

		List<ShoppingCartEntity> notExpiredCarts = this.shoppingCartDao.findByUserAndExpired(new UserEntity(email), false);
		
		if (notExpiredCarts.isEmpty())
			throw new ShoppingCartNotFoundException();
		
		ShoppingCartEntity entity = notExpiredCarts.get(0);
				
		if (!entity.isExpired())
			entity.setExpired(shoppingCartBoundary.isExpired());
		
		Map<String, Object> boundaryMP = shoppingCartBoundary.getMoreProperties();
		
		if (boundaryMP != null)
			entity.addMoreProperties(boundaryMP);
		
		Set<ProductBoundary> boundaryProducts = shoppingCartBoundary.getProducts();
		
		if (boundaryProducts != null)
			boundaryProducts.forEach(p -> {
				if (!this.ProductExistsById(p.getProductId()))
					throw new ProductNotFoundException();
				if (p.getAmount() < 0)
					throw new IllegalProductAmoutException();
				
				ProductEntity pe = this.toProductEntity(p);
				
				entity.removeProduct(pe);
				if (pe.getAmount() != 0)
					entity.addProduct(pe);
			});
		this.shoppingCartDao.save(entity);
	}

	@Override
	public ShoppingCartBoundary[] getAll(FilterTypeEnum filterType, String filterValue, SortAttrEnum sortBy,
			SortOrderEnum sortOrder, int size, int page) {
		
		Iterable<ShoppingCartEntity> res = null;
		PageRequest pageRequest = PageRequest.of(page,
				size,
				sortOrder == SortOrderEnum.ASC ? Direction.ASC : Direction.DESC,
				sortBy == SortAttrEnum.creation ? "creationTimestamp" : "user", "shoppingCartId");
		
		if (filterType == FilterTypeEnum.byNotExpired)
			res = this.shoppingCartDao.findByExpired(false, pageRequest);
		
		if (filterType == FilterTypeEnum.byUserEmail && !filterValue.isBlank())
			res = this.shoppingCartDao.findByUser(new UserEntity(filterValue), pageRequest);
			
		if (res == null)
			res = this.shoppingCartDao.findAll(pageRequest);
		
		return StreamSupport.stream(
				res.spliterator(), false)
					.map(this::toShoppingCartBoundary)
					.collect(Collectors.toList())
					.toArray(new ShoppingCartBoundary[0]);
	}

	@Override
	public void deleteAll() {
		this.shoppingCartDao.deleteAll();
	}

	private boolean ProductExistsById(String productId) {
		try {
			this.shoppingCatalogClient.getProductById(productId);
		} catch (NotFound e) {
			return false;
		} catch (Unauthorized e) {
			return false;
		}
			return true;

	}

	private boolean UserExistsByEmail(String email) {
		try {
			this.usersManagementClient.getUserByEmail(email);
		} catch (NotFound e) {
			return false;
		} catch (Unauthorized e) {
			return false;
		}
		return true;
	}
	
	private void validateProductBoundary(ProductBoundary p) {
		if (p == null)
			throw new IllegalProductException();
		if (p.getProductId() == null || p.getProductId().isBlank())
			throw new IllegalProductException();
		
		System.err.println(p);
		
		if (!this.ProductExistsById(p.getProductId()))
			throw new ProductNotFoundException();
		if (p.getAmount() <= 0)
			throw new IllegalProductAmoutException();
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
	
	private ProductEntity toProductEntity(ProductBoundary productBoundary) {
		return new ProductEntity(productBoundary.getProductId(), productBoundary.getAmount());
	}
	
}
