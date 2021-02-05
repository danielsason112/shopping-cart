package il.ac.afeka.cloud.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
		try {
			return shoppingCartsService.transactionalCreateShoppingCart(shoppingCartBoundary);
		} catch (InvalidDataAccessApiUsageException e) {
			return shoppingCartsService.createShoppingCart(shoppingCartBoundary);
		}
		
	}
	
	@RequestMapping(path = "/shoppingCarts/{emailOrShoppingCartId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ShoppingCartBoundary getShoppingCart(@PathVariable("emailOrShoppingCartId") String emailOrShoppingCartId) {
		return shoppingCartsService.getShoppingCart(emailOrShoppingCartId);
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
	 
	 @RequestMapping(path = "/shoppingCarts",
             method = RequestMethod.DELETE)
	 public void deleteAll() {
		 this.shoppingCartsService.deleteAll();
	 }
	 
	 
}
