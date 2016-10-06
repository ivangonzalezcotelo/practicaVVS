package es.udc.pa.pa007.auctionhouse.model.product;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface ProductDao extends GenericDao<Product, Long>{

	/**
     * Returns a Product List filtered by any key word (not product identifier) and category picked
     *
     * @param key the words to search and the category selected to be filtered
     * @return the List<Product>
     */
	public List<Product> findByKeyAndCategory(String key, Long categoryId, int startIndex, int count);
	
    /**
     * Returns a Product List by owner (not product identifier)
     *
     * @param owner of the product
     * @return the List<Product>
     */
    public List<Product> findByOwner(Long owner, int startIndex, int count);
    
    /**
     * Returns a count of Products of an owner (not product identifier)
     *
     * @param owner of the product
     * @return the count
     */
    public int getNumberOfProducts(Long userId);
    
    /**
     * Returns the number of Product that constains keywords and belong to the category (catId)
     *
     * @param keywords
     * @param id of the category
     * @return the count
     */
    public int getNumberOfSearchProducts(String keywords, Long catId);
    
}
