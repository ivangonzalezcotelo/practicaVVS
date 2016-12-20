package es.udc.pa.pa007.auctionhouse.model.productservice;

import java.math.BigDecimal;
import java.util.List;

import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * ProductService.
 *
 */
public interface ProductService {

	/**
	 * @param prodName
	 *            the product name.
	 * @param description
	 *            the description.
	 * @param launchPrice
	 *            the launch price.
	 * @param sendInfo
	 *            the send info.
	 * @param minsToFinish
	 *            the mins to finish the bid.
	 * @param owner
	 *            the owner.
	 * @param catId
	 *            the cat id.
	 * @return the Product.
	 * @throws InstanceNotFoundException
	 *             if owner or category are invalid.
	 */
	Product insertProduct(String prodName, String description, BigDecimal launchPrice, String sendInfo,
			int minsToFinish, Long owner, Long catId) throws InstanceNotFoundException;

	/**
	 * @param keywords
	 *            the keywords.
	 * @param catId
	 *            the category id.
	 * @param startIndex
	 *            the start position.
	 * @param count
	 *            the number of elements.
	 * @return the list product.
	 * @throws InstanceNotFoundException
	 *             if category is invalid.
	 */
	List<Product> findActiveAuctions(String keywords, Long catId, int startIndex, int count)
			throws InstanceNotFoundException;

	/**
	 * @param userId
	 *            the owner id.
	 * @param startIndex
	 *            the start position.
	 * @param count
	 *            the number of elements.
	 * @return the list products.
	 * @throws InstanceNotFoundException
	 *             if owner is invalid.
	 */
	List<Product> listProducts(Long userId, int startIndex, int count) throws InstanceNotFoundException;

	/**
	 * @param productId
	 *            the product id.
	 * @return the Product.
	 * @throws InstanceNotFoundException
	 *             if the product is invalid.
	 */
	Product findByProductId(Long productId) throws InstanceNotFoundException;

	/**
	 * @return the list of categories.
	 */
	List<Category> getAllCategories();

	/**
	 * @param categoryId
	 *            the category id.
	 * @return the Category.
	 * @throws InstanceNotFoundException
	 *             if category is invalid.
	 */
	Category findCategory(Long categoryId) throws InstanceNotFoundException;

	/**
	 * @param userId
	 *            the owner id.
	 * @return the number of elements.
	 * @throws InstanceNotFoundException
	 *             if owner is invalid.
	 */
	int getNumberOfProducts(Long userId) throws InstanceNotFoundException;

	/**
	 * @param keywords
	 *            the keywords.
	 * @param catId
	 *            the category id.
	 * @return the number of elements.
	 * @throws InstanceNotFoundException
	 *             if category is invalid.
	 */
	int getNumberOfSearhProducts(String keywords, Long catId) throws InstanceNotFoundException;
}
