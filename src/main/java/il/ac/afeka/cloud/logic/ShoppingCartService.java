package il.ac.afeka.cloud.logic;

import il.ac.afeka.cloud.enums.FilterTypeEnum;
import il.ac.afeka.cloud.enums.SortAttrEnum;
import il.ac.afeka.cloud.enums.SortOrderEnum;
import il.ac.afeka.cloud.layout.ShoppingCartBoundary;

public interface ShoppingCartService {

	public ShoppingCartBoundary createShoppingCart(ShoppingCartBoundary shoppingCartBoundary);

	public ShoppingCartBoundary getShoppingCart(String shoppingCartId);

	public ShoppingCartBoundary getShoppingCartByEmail(String email);

	public void updateShoppingCart(String email, ShoppingCartBoundary shoppingCartBoundary);

	public ShoppingCartBoundary[] getAll(FilterTypeEnum filterType, String filterValue, SortAttrEnum sortBy, SortOrderEnum sortOrder,
			int size, int page);

}
