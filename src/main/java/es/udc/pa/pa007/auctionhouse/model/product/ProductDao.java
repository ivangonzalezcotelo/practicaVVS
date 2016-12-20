package es.udc.pa.pa007.auctionhouse.model.product;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

/**
 * ProductDao.
 *
 */
public interface ProductDao extends GenericDao<Product, Long> {

	/**
	 * Returns a Product List filtered by any key word (not product identifier)
	 * and category picked.
	 *
	 * @param key
	 *            the words to search and the category selected to be filtered.
	 * @param categoryId
	 *            the category id.
	 * @param startIndex
	 *            the start position of list.
	 * @param count
	 *            the number of elements.
	 * @return the List<Product>.
	 */
	List<Product> findByKeyAndCategory(String key, Long categoryId, int startIndex, int count);

	/**
	 * Returns a Product List by owner (not product identifier).
	 *
	 * @param owner
	 *            of the product.
	 * @param startIndex
	 *            the start position of list.
	 * @param count
	 *            the number of elements.
	 * @return the List<Product>.
	 */
	List<Product> findByOwner(Long owner, int startIndex, int count);

	/**
	 * Returns a count of Products of an owner (not product identifier).
	 *
	 * @param userId
	 *            the user id.
	 * @return the count.
	 */
	int getNumberOfProducts(Long userId);

	/**
	 * Returns the number of Product that constains keywords and belong to the
	 * category (catId).
	 *
	 * @param keywords
	 *            the keywords.
	 * @param catId
	 *            the category Id.
	 * @return the count.
	 */
	int getNumberOfSearchProducts(String keywords, Long catId);

}
