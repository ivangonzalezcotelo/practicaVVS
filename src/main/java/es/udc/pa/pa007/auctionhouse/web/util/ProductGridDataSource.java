package es.udc.pa.pa007.auctionhouse.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ProductGridDataSource implements GridDataSource {

	private ProductService productService; 
	private Long userId;
	private List<Product> products;
	private int startIndex;
	private boolean userNotFound;

	public ProductGridDataSource(ProductService productService,Long userId) {
		
		this.productService = productService;
		this.userId = userId;

	}

    public int getAvailableRows() {
    	    	
    	try {		
			return productService.getNumberOfProducts(userId);
		} catch (InstanceNotFoundException e) {
			userNotFound = true;
			return 0;
		}
        
    }

    public Class<Product> getRowType() {
        return Product.class;
    }

    public Object getRowValue(int index) {
        return products.get(index-this.startIndex);
    }

    public void prepare(int startIndex, int endIndex,
    	List<SortConstraint> sortConstraints) {

        try {
        	
        	products = productService.listProducts(userId, startIndex, endIndex-startIndex+1);
	        this.startIndex = startIndex;

		} catch (InstanceNotFoundException e) {
		}

    }

    public boolean getUserNotFound() {
    	return userNotFound;
    }

}
