package es.udc.pa.pa007.auctionhouse.test.model.productservice;

import static es.udc.pa.pa007.auctionhouse.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa007.auctionhouse.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pa.pa007.auctionhouse.model.bidservice.InvalidBidException;
import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.category.CategoryDao;
import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.product.ProductDao;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserProfileDetails;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class ProductServiceTest {
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductService productServices;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private CategoryDao categoryDao;
	
	/* PR-IN-007 */
	
	@Test
	public void testInsertProductAndListProducts() 
			throws DuplicateInstanceException, InstanceNotFoundException{
		
		UserProfile owner = userService.registerUser(
				"owner", "userPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));
		
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);
		
		Product product = productServices.insertProduct( "Canon PowerShot S50", "Camara Fotografica", new BigDecimal(10), 
				"Lugar de envio", 2, owner.getUserProfileId(),camaras.getCatId());
				
		List <Product> products= productServices.listProducts(owner.getUserProfileId(), 0, 10) ;	
		
		assertEquals( 1, products.size() );
		assertEquals( product.getProdName(), products.get(0).getProdName() );
		assertEquals(owner.getLoginName(), products.get(0).getOwner().getLoginName());
	}
	
	/* PR-IN-008 */
	
	@Test
	public void testNoListProducts()
			throws DuplicateInstanceException, InstanceNotFoundException{
		UserProfile owner = userService.registerUser(
			"owner", "userPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));
		
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);
		
		List <Product> products= productServices.listProducts(owner.getUserProfileId(), 0, 10) ;	
		assertEquals( 0 , products.size());
	}
	
	/* PR-IN-009 */
	
	@Test
	public void testInsertBadProduct()
			throws DuplicateInstanceException, InstanceNotFoundException{
		
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));

		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);
		
		Product p1 = productServices.insertProduct("Canon PowerShot S50", "Camara Fotografica", new BigDecimal(-10), 
				"Lugar", 2, owner.getUserProfileId(), camaras.getCatId());
		
		Product p2 = productServices.insertProduct("Canon PowerShot S50", "Camara Fotografica", new BigDecimal(-10), 
				"Lugar", -5, owner.getUserProfileId(), camaras.getCatId());
		
		Product p3 = productServices.insertProduct("Canon PowerShot S50", "Camara Fotografica", new BigDecimal(10), 
				"Lugar", -5, owner.getUserProfileId(), camaras.getCatId());
			
		assertEquals(p1, null);
		assertEquals(p2, null);
		assertEquals(p3, null);
	}
	
	/* PR-IN-010 */
	
	@Test
	public void testFindAllActiveAuctions()
			throws DuplicateInstanceException, InstanceNotFoundException{

		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));
		
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);		
			
		Product product = productServices.insertProduct("Canon PowerShot S50", "Camara Fotografica", new BigDecimal(10), 
				"Lugar de envio", 2, owner.getUserProfileId(), camaras.getCatId());
		Product product2 = productServices.insertProduct("Canon PowerShot N2", "Camara Fotografica", new BigDecimal(10), 
				"Lugar 2", 10, owner.getUserProfileId(), camaras.getCatId());
		
		/*Expired Auction*/
		Calendar before = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		before.add(Calendar.MINUTE, -10);
		Product expiredProduct = new Product("Canon EOS 100D", "Camara Fotografica", "Correo", 
				new BigDecimal(10), before, before, owner,camaras);
		productDao.save(expiredProduct);
		
		List <Product> products= productServices.findActiveAuctions("",null, 0, 10) ;	
		
		assertEquals(2 , products.size());
		assertEquals(product.getProdName() , products.get(0).getProdName());
		assertEquals(product2.getProdName() , products.get(1).getProdName());
	}
	
	/* PR-IN-011 */
	
	@Test
	public void testfindNoActiveAuctions()
			throws InstanceNotFoundException, DuplicateInstanceException{
		
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));
		
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);
		
		/*Expired Auction*/
		Calendar before = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		before.add(Calendar.MINUTE, -10);
		Product expiredProduct = new Product("Canon EOS 100D", "Camara Fotografica", "Correo", 
				new BigDecimal(10), before, before, owner,camaras);
		productDao.save(expiredProduct);
		
		List <Product> products= productServices.findActiveAuctions("Canon 100",camaras.getCatId(), 0, 10 ) ;	
		
		assertEquals( 0, products.size() ); 
	}
	
	/* PR-IN-012 */
	
	@Test
	public void testFindByCategory() 
			throws DuplicateInstanceException, InstanceNotFoundException{
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));
		
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);		
		
		Category informatica = new Category ("Informatica");
		categoryDao.save(informatica);
		
		Product product = productServices.insertProduct("Canon PowerShot S50", "Camara de fotos", new BigDecimal(100), 
				"Lugar envio", 2, owner.getUserProfileId(), camaras.getCatId());
		productServices.insertProduct("Raton inalambrico", "Raton sin cables", new BigDecimal(20), 
				"Lugar envio", 10, owner.getUserProfileId(), informatica.getCatId());
		
		List <Product> products= productServices.findActiveAuctions("",camaras.getCatId(), 0, 10) ;
		
		assertEquals(1, products.size());
		assertEquals(product.getProdName(), products.get(0).getProdName());
		assertEquals(product.getCategory().getCatName(), products.get(0).getCategory().getCatName());
	}
	
	/* PR-IN-013 */
	
	@Test
	public void testFindBySeveralKeys() throws DuplicateInstanceException, InstanceNotFoundException{
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));
		
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);		
		
		Product product = productServices.insertProduct("Canon PowerShot S50", "Camara de fotos", new BigDecimal(100), 
				"Lugar envio", 2, owner.getUserProfileId(), camaras.getCatId());
		Product product2 = productServices.insertProduct("Canon PowerShot N2", "Camara de fotos", new BigDecimal(20), 
				"Lugar envio", 10, owner.getUserProfileId(), camaras.getCatId());
		productServices.insertProduct("Canon EOS 100D", "Camara de fotos", new BigDecimal(20), 
				"Lugar envio", 10, owner.getUserProfileId(), camaras.getCatId());
		
		List <Product> products= productServices.findActiveAuctions("CAN power", null, 0, 10) ;

		assertEquals(2, products.size());
		assertEquals(product.getProdName(), products.get(0).getProdName());
		assertEquals(product2.getProdName(), products.get(1).getProdName());
	}
	
	/* PR-IN-014 */
	
	@Test
	public void testFindBySeveralKeysAndCategory() 
			throws DuplicateInstanceException, InstanceNotFoundException{
		
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));
		
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);		
		
		Category informatica = new Category ("Informatica");
		categoryDao.save(informatica);
		
		Product product = productServices.insertProduct("Canon PowerShot S50", "Camara de fotos", new BigDecimal(100), 
				"Lugar envio", 2, owner.getUserProfileId(), camaras.getCatId());
		Product product2 = productServices.insertProduct("Canon PowerShot N2", "Camara de fotos", new BigDecimal(100), 
				"Lugar envio", 2, owner.getUserProfileId(), camaras.getCatId());
		productServices.insertProduct("Raton inalambrico", "Raton sin cables", new BigDecimal(20), 
				"Lugar envio", 10, owner.getUserProfileId(), informatica.getCatId());
		
		List <Product> products= productServices.findActiveAuctions("CAN power",camaras.getCatId(), 0, 10) ;
		
		assertEquals(2, products.size());
		assertEquals(product.getProdName(), products.get(0).getProdName());
		assertEquals(product.getCategory().getCatName(), products.get(0).getCategory().getCatName());
		assertEquals(product2.getProdName(), products.get(1).getProdName());
		assertEquals(product2.getCategory().getCatName(), products.get(1).getCategory().getCatName());
	}
	
	/* PR-IN-015 */
	
	@Test
	public void testFindProductId() 
			throws DuplicateInstanceException, InstanceNotFoundException{
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));
		
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);		
		
		Category informatica = new Category ("Informatica");
		categoryDao.save(informatica);
		
		Product product = productServices.insertProduct("Canon PowerShot S50", "Camara de fotos", new BigDecimal(100), 
				"Lugar envio", 2, owner.getUserProfileId(), camaras.getCatId());
		productServices.insertProduct("Canon PowerShot N2", "Camara de fotos", new BigDecimal(100), 
				"Lugar envio", 2, owner.getUserProfileId(), camaras.getCatId());
		Product search = productServices.findByProductId(product.getProdId());
		
		assertEquals(product.getProdId(), search.getProdId());
		assertEquals(product.getProdName(), search.getProdName());
	}
	
	/* PR-IN-016 */
	
	@Test(expected = InstanceNotFoundException.class)
	public void testNoFindProductId() 
			throws DuplicateInstanceException, InstanceNotFoundException{
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));
		
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);		
		
		productServices.insertProduct("Canon PowerShot S50", "Camara de fotos", new BigDecimal(100), 
				"Lugar envio", 2, owner.getUserProfileId(), camaras.getCatId());
		productServices.findByProductId(new Long(23));
	}
	
	/* PR-IN-017 */
	
	@Test
	public void testGetAllCategorys(){
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);		
		
		Category informatica = new Category ("Informatica");
		categoryDao.save(informatica);
		
		List <Category> categorys = productServices.getAllCategories();
		
		assertEquals(2, categorys.size());
		assertEquals(camaras.getCatName(), categorys.get(0).getCatName());
		assertEquals(informatica.getCatName(), categorys.get(1).getCatName());
	}
	
	/* PR-IN-018 */
	
	@Test
	public void testFindCategory() 
			throws InstanceNotFoundException{
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);		
		
		Category informatica = new Category ("Informatica");
		categoryDao.save(informatica);
		
		Category category = productServices.findCategory(camaras.getCatId());
		
		assertEquals(camaras.getCatId(), category.getCatId());
		assertEquals(camaras.getCatName(), category.getCatName());
	}
	
	/* PR-IN-019 */
	
	@Test(expected = InstanceNotFoundException.class)
	public void testNoFindCategory() 
			throws InstanceNotFoundException{
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);		
		
		Category informatica = new Category ("Informatica");
		categoryDao.save(informatica);
		
		productServices.findCategory(new Long(53));
	}
	
	/* PR-IN-020 */
	
	@Test
	public void testNoGetAllCategorys(){
		List <Category> categorys = productServices.getAllCategories();
		
		assertEquals(0, categorys.size());
	}
	
	@Test
	public void checkSettersGetters() 
			throws DuplicateInstanceException, InstanceNotFoundException{

		UserProfile owner = userService.registerUser(
				"owner", "userPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));
		
		Category cat = new Category("Camaras Fotograficas");
		categoryDao.save(cat);
		
		Product product = productServices.insertProduct( "Canon PowerShot S50", "Camara Fotografica", new BigDecimal(10), 
				"Lugar de envio", 2, owner.getUserProfileId(),cat.getCatId());
					
		Product product2 = new Product();
		product2.setActualPrice(product.getActualPrice());
		product2.setCategory(product.getCategory());
		product2.setCreateDate(product.getCreateDate());
		product2.setDescription(product.getDescription());
		product2.setFinishDate(product.getFinishDate());
		product2.setLaunchPrice(product.getLaunchPrice());
		product2.setOwner(product.getOwner());
		product2.setProdId(product.getProdId());
		product2.setProdName(product.getProdName());
		product2.setSendInfo(product.getSendInfo());
		product2.setVersion(product.getVersion());
		product2.setWinnerBid(product.getWinnerBid());
		
		assertEquals(product.getActualPrice(),product2.getActualPrice());
		assertEquals(product.getCategory(),product2.getCategory());
		assertEquals(product.getCreateDate(),product2.getCreateDate());
		assertEquals(product.getDescription(),product2.getDescription());
		assertEquals(product.getFinishDate(),product2.getFinishDate());
		assertEquals(product.getLaunchPrice(),product2.getLaunchPrice());
		assertEquals(product.getOwner(),product2.getOwner());
		assertEquals(product.getProdId(),product2.getProdId());
		assertEquals(product.getSendInfo(),product2.getSendInfo());
		assertEquals(product.getVersion(),product2.getVersion());
		assertEquals(product.getWinnerBid(),product2.getWinnerBid());
		
		Category cat2 = new Category();
		cat2.setCatId(cat.getCatId());
		cat2.setCatName(cat.getCatName());
		
		assertEquals(cat.getCatId(),cat2.getCatId());
		assertEquals(cat.getCatName(),cat2.getCatName());
	}
}
