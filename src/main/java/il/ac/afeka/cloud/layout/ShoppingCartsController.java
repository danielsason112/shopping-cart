package il.ac.afeka.cloud.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import il.ac.afeka.cloud.enums.FilterTypeEnum;
import il.ac.afeka.cloud.enums.SortAttrEnum;
import il.ac.afeka.cloud.enums.SortOrderEnum;
import il.ac.afeka.cloud.logic.ShoppingCartService;

@RestController
public class ShoppingCartsController {
	private ShoppingCartService shoppingCartsService;

	@Autowired
	public ShoppingCartsController(ShoppingCartService shoppingCartsService) {
		this.shoppingCartsService = shoppingCartsService;
	}
	
	@RequestMapping(path = "/shoppingCarts",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ShoppingCartBoundary createShoppingCart(@RequestBody ShoppingCartBoundary shoppingCartBoundary) {
		return shoppingCartsService.createShoppingCart(shoppingCartBoundary);
	}
	
	@RequestMapping(path = "/shoppingCarts/byId/{shoppingCartId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ShoppingCartBoundary getShoppingCart(@PathVariable("shoppingCartId") String shoppingCartId) {
		return shoppingCartsService.getShoppingCart(shoppingCartId);
	}
	
	@RequestMapping(path = "/shoppingCarts/{email}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ShoppingCartBoundary getShoppingCartByEmail(@PathVariable("email") String email) {
		return shoppingCartsService.getShoppingCartByEmail(email);
	}
	
	@RequestMapping(path = "/shoppingCarts/{email}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateShoppingCart(@PathVariable("email") String email, @RequestBody ShoppingCartBoundary shoppingCartBoundary) {
		shoppingCartsService.updateShoppingCart(email, shoppingCartBoundary);
	}
	
	 @RequestMapping(path = "/shoppingCarts",
             method = RequestMethod.GET,
             produces = MediaType.APPLICATION_JSON_VALUE)
	 public ShoppingCartBoundary[] getAll(
			 @RequestParam(name = "filterType", required = false) FilterTypeEnum filterType,
			 @RequestParam(name = "filterValue", required = false, defaultValue = "") String filterValue,
			 @RequestParam(name = "sortBy", required = false, defaultValue = "creation") SortAttrEnum sortBy,
	         @RequestParam(name = "sortOrder", required = false, defaultValue = "DESC") SortOrderEnum sortOrder,
	         @RequestParam(name = "size", required = false, defaultValue = "10") int size,
	         @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		 
		 return shoppingCartsService.getAll(
				 filterType,
				 filterValue,
				 sortBy == null ? SortAttrEnum.creation : sortBy,
				 sortBy == null ? SortOrderEnum.DESC : sortOrder == null ? SortOrderEnum.ASC : sortOrder,
						 size,
						 page);
	 }
	 
	 
}
