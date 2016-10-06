package es.udc.pa.pa007.auctionhouse.model.productservice;

import java.math.BigDecimal;
import java.util.List;

import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface ProductService {
	public Product insertProduct(String prodName, String description,
            BigDecimal launchPrice, String sendInfo, int minsToFinish,
            Long owner, Long catId) throws InstanceNotFoundException;

    public List<Product> findActiveAuctions(String keywords, Long catId, int startIndex, int count) throws InstanceNotFoundException;

    public List<Product> listProducts(Long userId, int startIndex, int count) throws InstanceNotFoundException;
    
    public Product findByProductId(Long productId) throws InstanceNotFoundException;
    
    public List<Category> getAllCategories();
    
    public Category findCategory(Long categoryId) throws InstanceNotFoundException;
    
    public int getNumberOfProducts(Long userId) throws InstanceNotFoundException;
    
    public int getNumberOfSearhProducts(String keywords,Long catId) throws InstanceNotFoundException;
}


