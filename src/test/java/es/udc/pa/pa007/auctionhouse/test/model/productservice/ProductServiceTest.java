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
	
	@Test
	public void testInsertBadProduct()
			throws DuplicateInstanceException, InstanceNotFoundException{
		
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));

		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);
		
		productServices.insertProduct("Canon PowerShot S50", "Camara Fotografica", new BigDecimal(-10), 
				"Lugar", 2, owner.getUserProfileId(), camaras.getCatId());
		
		List <Product> products= productServices.listProducts(owner.getUserProfileId(), 0, 10) ;	
		
		assertEquals( 0 , products.size());
	}
	
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
	
	@Test(expected = InstanceNotFoundException.class)
	public void testNoFindCategory() 
			throws InstanceNotFoundException{
		Category camaras = new Category("Camaras Fotograficas");
		categoryDao.save(camaras);		
		
		Category informatica = new Category ("Informatica");
		categoryDao.save(informatica);
		
		productServices.findCategory(new Long(53));
	}
	
	@Test
	public void testNoGetAllCategorys(){
		List <Category> categorys = productServices.getAllCategories();
		
		assertEquals(0, categorys.size());
	}
}
