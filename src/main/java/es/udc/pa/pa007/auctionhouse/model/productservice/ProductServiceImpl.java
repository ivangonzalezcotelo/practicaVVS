package es.udc.pa.pa007.auctionhouse.model.productservice;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfileDao;
import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.category.CategoryDao;
import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.product.ProductDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private CategoryDao catDao;
	
	@Autowired
	private UserProfileDao userProfileDao;
	
	@Autowired
	private ProductDao productDao;
	
	public Product insertProduct(String prodName, String description,
			BigDecimal launchPrice, String sendInfo, int minsToFinish,
			Long owner, Long catId) throws InstanceNotFoundException {
		
		Category cat = catDao.find(catId);
		UserProfile user = userProfileDao.find(owner);
		
		// Para evitar valores negativos
		if((launchPrice.signum() != 1) || (minsToFinish < 1))
			return null;
		
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		Calendar finish = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		finish.add(Calendar.MINUTE, minsToFinish);
		Product prod = new Product(prodName, description, sendInfo,
				launchPrice, now, finish, user, cat);
		
		productDao.save(prod);
		return prod;
	}
	
	@Transactional(readOnly = true)
	public List<Product> findActiveAuctions(String keywords, Long catId, int startIndex, int count) throws InstanceNotFoundException{
		if ( catId != null ){
			catDao.find(catId);
		}
		return productDao.findByKeyAndCategory(keywords, catId, startIndex, count);		
	}
	
	@Transactional(readOnly = true)
	public List<Product> listProducts(Long userId, int startIndex, int count) throws InstanceNotFoundException{
		return productDao.findByOwner(userProfileDao.find(userId).getUserProfileId(), startIndex, count);
	}

	@Transactional(readOnly = true)
	public Product findByProductId(Long productId)
			throws InstanceNotFoundException {
		return productDao.find(productId);
	}

	@Transactional(readOnly = true)
	public List<Category> getAllCategories() {
		return catDao.getAllCategories();
	}

	@Transactional(readOnly = true)
	public Category findCategory(Long categoryId)
			throws InstanceNotFoundException {
		return catDao.find(categoryId);
	}

	@Transactional(readOnly = true)
	public int getNumberOfProducts(Long userId) throws InstanceNotFoundException {

		/* Check if account exists. */
		userProfileDao.find(userId);
			
		/* Return count. */
		return productDao.getNumberOfProducts(userId);

	}

	@Transactional(readOnly = true)
	public int getNumberOfSearhProducts(String keywords, Long catId)
			throws InstanceNotFoundException {
		if (catId != null){
			catDao.find(catId);
		}
		
		return productDao.getNumberOfSearchProducts(keywords, catId);
	}

}