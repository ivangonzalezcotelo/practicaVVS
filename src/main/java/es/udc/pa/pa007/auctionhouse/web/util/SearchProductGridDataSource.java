package es.udc.pa.pa007.auctionhouse.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * SearchProductGridDataSource.
 *
 */
public class SearchProductGridDataSource implements GridDataSource {

	/**
	 * The ProductService.
	 */
	private ProductService productService;

	/**
	 * The keys.
	 */
	private String keys;

	/**
	 * The category Id.
	 */
	private Long catId;

	/**
	 * The startIndex.
	 */
	private int startIndex;

	/**
	 * The selection.
	 */
	private List<Product> selection;

	/**
	 * @param productService
	 *            the ProductService.
	 * @param catId
	 *            the category Id.
	 * @param keys
	 *            the keys.
	 */
	public SearchProductGridDataSource(ProductService productService, Long catId, String keys) {
		this.productService = productService;
		this.catId = catId;
		if (keys == null) {
			this.keys = "";
		} else {
			this.keys = keys;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAvailableRows() {
		try {
			return productService.getNumberOfSearhProducts(keys, catId);

		} catch (InstanceNotFoundException e) {
			return 0;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Class getRowType() {
		return Product.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getRowValue(int arg0) {
		return selection.get(arg0 - startIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepare(int startIndex, int count, List<SortConstraint> arg2) {
		try {
			this.selection = this.productService.findActiveAuctions(keys, catId, startIndex, count - startIndex + 1);

			this.startIndex = startIndex;
		} catch (InstanceNotFoundException e) {
		}
	}
}
