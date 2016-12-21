package es.udc.pa.pa007.auctionhouse.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * ProductGridDataSource.
 *
 */
public class ProductGridDataSource implements GridDataSource {

	/**
	 * The ProductService.
	 */
	private ProductService productService;
	/**
	 * The userProfileId.
	 */
	private Long userId;
	/**
	 * The products.
	 */
	private List<Product> products;
	/**
	 * The start index.
	 */
	private int startIndex;
	/**
	 * The boolean userNotFound.
	 */
	private boolean userNotFound;

	/**
	 * @param productService
	 *            the ProductService.
	 * @param userId
	 *            the userProfile Id.
	 */
	public ProductGridDataSource(ProductService productService, Long userId) {

		this.productService = productService;
		this.userId = userId;

	}

	/**
	 * @return the available rows.
	 */
	public int getAvailableRows() {

		try {
			return productService.getNumberOfProducts(userId);
		} catch (InstanceNotFoundException e) {
			userNotFound = true;
			return 0;
		}

	}

	/**
	 * @return the row type.
	 */
	public Class<Product> getRowType() {
		return Product.class;
	}

	/**
	 * @param index
	 *            the index.
	 * @return the row value.
	 */
	public Object getRowValue(int index) {
		return products.get(index - this.startIndex);
	}

	/**
	 * @param startIndex
	 *            the start index.
	 * @param endIndex
	 *            the end index.
	 * @param sortConstraints
	 *            the sortConstraints.
	 */
	public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {

		try {

			products = productService.listProducts(userId, startIndex, endIndex - startIndex + 1);
			this.startIndex = startIndex;

		} catch (InstanceNotFoundException e) {
		}

	}

	/**
	 * @return the boolean userNotFound.
	 */
	public boolean getUserNotFound() {
		return userNotFound;
	}

}
