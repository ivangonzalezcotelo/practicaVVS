package es.udc.pa.pa007.auctionhouse.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class SearchProductGridDataSource implements GridDataSource{
	
	private ProductService productService;
			
	private String keys;
	
	private Long catId;
	
	private int startIndex;
	
	private List<Product> selection;
	
	public SearchProductGridDataSource(ProductService productService, Long catId, String keys){
		this.productService = productService;
		this.catId = catId;
		if (keys == null){
			this.keys = "";
		}else{
			this.keys = keys;
		}
		
	}
	
	@Override
	public int getAvailableRows(){
		try {
			return productService.getNumberOfSearhProducts(keys, catId);
			
		} catch (InstanceNotFoundException e) {return 0;	}
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getRowType() {
		return Product.class;
	}

	@Override
	public Object getRowValue(int arg0) {
		return selection.get(arg0-startIndex);
	}

	@Override
	public void prepare(int startIndex, int count, List<SortConstraint> arg2) {
		try {
			this.selection = this.productService.findActiveAuctions(keys, catId, startIndex, count - startIndex + 1);
			
			this.startIndex = startIndex;
		} catch (InstanceNotFoundException e) {}
	}
}
