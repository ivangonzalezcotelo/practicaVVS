package es.udc.pa.pa007.auctionhouse.test.model.productservice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.category.CategoryDao;
import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.product.ProductDao;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductServiceImpl;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceUnitTest {
	
	@InjectMocks
	ProductServiceImpl productService = new ProductServiceImpl();
	
	@Mock
	CategoryDao catDao;
	
	@Mock
	ProductDao productDao;
	
	@Mock
	UserProfileDao userProfileDao;
	
	//Insertar producto con categoria y usuario correcto
	//PR-UN-032
	@Test
	public void insertProductTest() throws InstanceNotFoundException{
		UserProfile user1 = new UserProfile("user1","pass", "jason", "petersen","jason@gmail.com");
		user1.setUserProfileId(Long.valueOf(1));
		Category cat = new Category("informatica");
		cat.setCatId(Long.valueOf(1));
		String prodName = "Ordenador portatil";
		
		when(userProfileDao.find(user1.getUserProfileId())).thenReturn(user1);
		when(catDao.find(cat.getCatId())).thenReturn(cat);
		
		Product product = productService.insertProduct(prodName, "Super tocho", BigDecimal.valueOf(5), "Por Correo", 5 , user1.getUserProfileId(), cat.getCatId());

		verify(productDao, times(1)).save(any(Product.class));
		assertEquals(prodName,product.getProdName());
	}
	
	//Insertar producto con categoria incorrecta
	//PR-UN-033
	@Test(expected = InstanceNotFoundException.class)
	public void insertProductIncorrectCatTest() throws InstanceNotFoundException{
		UserProfile user1 = new UserProfile("user1","pass", "jason", "petersen","jason@gmail.com");
		user1.setUserProfileId(Long.valueOf(1));
		Long catId = Long.valueOf(1);
		
		when(catDao.find(catId)).thenThrow(new InstanceNotFoundException("category", Category.class.getName()));
		
		productService.insertProduct("Ordenador portatil", "Super tocho", BigDecimal.valueOf(5), "Por Correo", 5 , user1.getUserProfileId(), catId);
		
	}
	
	//Insertar producto con usuario incorrecto
	//PR-UN-034
	@Test(expected = InstanceNotFoundException.class)
	public void insertProductIncorrectOwnerTest() throws InstanceNotFoundException{
		Long userId = Long.valueOf(1);
		Category cat = new Category("informatica");
		cat.setCatId(Long.valueOf(1));
		
		when(catDao.find(cat.getCatId())).thenReturn(cat);
		when(userProfileDao.find(userId)).thenThrow(new InstanceNotFoundException("user", Category.class.getName()));
		
		productService.insertProduct("Ordenador portatil", "Super tocho", BigDecimal.valueOf(5), "Por Correo", 5 , userId, cat.getCatId());
		
	}
	
	//Insertar producto con precio negativo
	//PR-UN-035
	@Test
	public void insertProductNegativePriceTest() throws InstanceNotFoundException{
		UserProfile user1 = new UserProfile("user1","pass", "jason", "petersen","jason@gmail.com");
		user1.setUserProfileId(Long.valueOf(1));
		Category cat = new Category("informatica");
		cat.setCatId(Long.valueOf(1));
		String prodName = "Ordenador portatil";
		
		when(userProfileDao.find(user1.getUserProfileId())).thenReturn(user1);
		when(catDao.find(cat.getCatId())).thenReturn(cat);
		
		Product product = productService.insertProduct(prodName, "Super tocho", BigDecimal.valueOf(-5), "Por Correo", 5 , user1.getUserProfileId(), cat.getCatId());
		
		assertNull(product);
		
	}
	
	//Buscar productos con sus distintas variantes
	//PR-UN-036
	@Test
	public void findActiveAuctionsTest() throws InstanceNotFoundException{
		UserProfile user1 = new UserProfile("user1","pass", "jason", "petersen","jason@gmail.com");
		user1.setUserProfileId(Long.valueOf(1));
		Category cat = new Category("informatica");
		cat.setCatId(Long.valueOf(1));
		String prodName = "Ordenador portatil";
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		Calendar finish = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		finish.add(Calendar.MINUTE, 50);
		Product product = new Product(prodName, "Super tocho","Por correo", BigDecimal.valueOf(5), now, finish, user1, cat);
		List<Product> products = new ArrayList<Product>();
		products.add(product);

		when(productDao.findByKeyAndCategory("", null, 0, 10)).thenReturn(products);
		when(productDao.findByKeyAndCategory("nad", null, 0, 10)).thenReturn(products);
		when(productDao.findByKeyAndCategory("", cat.getCatId(), 0, 10)).thenReturn(products);
		when(productDao.findByKeyAndCategory("nad", cat.getCatId(), 0, 10)).thenReturn(products);
		
		List<Product> products2 = productService.findActiveAuctions("", null, 0, 10);
		
		assertEquals(products.size(), products2.size());
		assertEquals(prodName, products2.get(0).getProdName());
		
		products2 = productService.findActiveAuctions("nad", null, 0, 10);
		
		assertEquals(products.size(), products2.size());
		assertEquals(prodName, products2.get(0).getProdName());
		
		products2 = productService.findActiveAuctions("", cat.getCatId(), 0, 10);
		
		assertEquals(products.size(), products2.size());
		assertEquals(prodName, products2.get(0).getProdName());
		
		
		products2 = productService.findActiveAuctions("nad", cat.getCatId(), 0, 10);

		assertEquals(products.size(), products2.size());
		assertEquals(prodName, products2.get(0).getProdName());
		
		
	}

	//Buscar productos categoria invalida
	//PR-UN-037
	@Test(expected = InstanceNotFoundException.class)
	public void findActiveAuctionsInvalidCategoryTest() throws InstanceNotFoundException{
		Long l = Long.valueOf(2);

		when(catDao.find(l)).thenThrow(new InstanceNotFoundException("category", Category.class.getName()));
		
		productService.findActiveAuctions("", l, 0, 10);
		
	}
	
	//Listar productos de un usuario
	//PR-UN-038
	@Test
	public void listProductsTest() throws InstanceNotFoundException{
		UserProfile user1 = new UserProfile("user1","pass", "jason", "petersen","jason@gmail.com");
		user1.setUserProfileId(Long.valueOf(1));
		Category cat = new Category("informatica");
		cat.setCatId(Long.valueOf(1));
		String prodName = "Ordenador portatil";
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		Calendar finish = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		finish.add(Calendar.MINUTE, 50);
		Product product = new Product(prodName, "Super tocho","Por correo", BigDecimal.valueOf(5), now, finish, user1, cat);
		List<Product> products = new ArrayList<Product>();
		products.add(product);
		
		when(productDao.findByOwner(user1.getUserProfileId(), 0, 10)).thenReturn(products);
		when(userProfileDao.find(user1.getUserProfileId())).thenReturn(user1);
		
		List<Product> products2 = productService.listProducts(user1.getUserProfileId(), 0, 10);
		
		assertEquals(products.size(), products2.size());
		assertEquals(prodName, products2.get(0).getProdName());
	}
	
	//Listar productos de un usuario no valido
	//PR-UN-039
	@Test(expected= InstanceNotFoundException.class)
	public void listProductsUserInvalidTest() throws InstanceNotFoundException{

		Long l = Long.valueOf(2);
		
		when(userProfileDao.find(l)).thenThrow(new InstanceNotFoundException("user", UserProfile.class.getName()));
		
		productService.listProducts(l, 0, 10);
		
	}
	
	//Buscar un producto invalido
	//PR-UN-040
	@Test(expected= InstanceNotFoundException.class)
	public void findByProductIdInvalidTest() throws InstanceNotFoundException{

		Long l = Long.valueOf(2);
		
		when(productDao.find(l)).thenThrow(new InstanceNotFoundException("product", Product.class.getName()));
		
		productService.findByProductId(l);
	}
	
	//Buscar un producto
	//PR-UN-041
	@Test
	public void findByProductIdTest() throws InstanceNotFoundException{
		UserProfile user1 = new UserProfile("user1","pass", "jason", "petersen","jason@gmail.com");
		Category cat = new Category("informatica");
		String prodName = "Ordenador portatil";
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		Calendar finish = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		finish.add(Calendar.MINUTE, 50);
		Product product = new Product(prodName, "Super tocho","Por correo", BigDecimal.valueOf(5), now, finish, user1, cat);
		product.setProdId(Long.valueOf(1));
		
		when(productDao.find(product.getProdId())).thenReturn(product);
		
		Product p = productService.findByProductId(product.getProdId());
		
		assertEquals(product.getProdId(), p.getProdId());
	}	
	
	//Listar las categorias
	//PR-UN-042
	@Test
	public void getAllCategoriesTest(){
		Category cat = new Category("informatica");
		List<Category> categories = new ArrayList<Category>();
		categories.add(cat);
		
		when(catDao.getAllCategories()).thenReturn(categories);
		
		List<Category> c = productService.getAllCategories();
		
		assertEquals(categories.size(), c.size());
		assertEquals(categories.get(0).getCatName(),c.get(0).getCatName());
	}	
	
	
	//Buscar categoria
	//PR-UN-043
	@Test
	public void findCategoryTest() throws InstanceNotFoundException{
		Category cat = new Category("informatica");
		Long l = Long.valueOf(1);
		cat.setCatId(l);
		
		when(catDao.find(l)).thenReturn(cat);
		
		Category c = productService.findCategory(cat.getCatId());
		
		assertEquals(cat.getCatId(), c.getCatId());
		assertEquals(cat.getCatName(),c.getCatName());
	}	
	
	//Buscar categoria incorrecta
	//PR-UN-044
	@Test(expected = InstanceNotFoundException.class)
	public void findCategoryInvalidTest() throws InstanceNotFoundException{

		Long m = Long.valueOf(2);
		
		when(catDao.find(m)).thenThrow(new InstanceNotFoundException("category", Category.class.getName()));
		
		productService.findCategory(m);
	}	
	
	//Obtener el numero de productos de un usuario
	//PR-UN-045
	@Test
	public void getNumberOfProductsTest() throws InstanceNotFoundException{
		UserProfile user1 = new UserProfile("user1","pass", "jason", "petersen","jason@gmail.com");
		user1.setUserProfileId(Long.valueOf(1));
		Category cat = new Category("informatica");
		cat.setCatId(Long.valueOf(1));
		String prodName = "Ordenador portatil";
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		Calendar finish = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		finish.add(Calendar.MINUTE, 50);
		new Product(prodName, "Super tocho","Por correo", BigDecimal.valueOf(5), now, finish, user1, cat);
				
		when(userProfileDao.find(user1.getUserProfileId())).thenReturn(user1);
		when(productDao.getNumberOfProducts(user1.getUserProfileId())).thenReturn(1);
		
		int n = productService.getNumberOfProducts(user1.getUserProfileId());
		
		assertEquals(1,n);
	}	
	
	//Obtener el numero de productos de un usuario incorrecto
	//PR-UN-046
	@Test(expected = InstanceNotFoundException.class)
	public void getNumberOfProductsInvalidTest() throws InstanceNotFoundException{

		Long l = Long.valueOf(5);
				
		when(userProfileDao.find(l)).thenThrow(new InstanceNotFoundException("userProfile", UserProfile.class.getName()));
		productService.getNumberOfProducts(l);
		
		
	}	

	//Obtener el numero de productos de una busqueda
	//PR-UN-047
	@Test
	public void getNumberOfSearhProductsTest() throws InstanceNotFoundException{
		UserProfile user1 = new UserProfile("user1","pass", "jason", "petersen","jason@gmail.com");
		user1.setUserProfileId(Long.valueOf(1));
		Category cat = new Category("informatica");
		cat.setCatId(Long.valueOf(1));
		Category cat2 = new Category("musica");
		cat.setCatId(Long.valueOf(2));
		String prodName = "Ordenador portatil";
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		Calendar finish = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		finish.add(Calendar.MINUTE, 50);
		new Product(prodName, "Super tocho","Por correo", BigDecimal.valueOf(5), now, finish, user1, cat);
		new Product(prodName, "Molon","Por correo", BigDecimal.valueOf(5), now, finish, user1, cat2);
		
		when(catDao.find(cat.getCatId())).thenReturn(cat);
		when(productDao.getNumberOfSearchProducts("", cat.getCatId())).thenReturn(1);
		when(productDao.getNumberOfSearchProducts("", null)).thenReturn(2);
		
		int n = productService.getNumberOfSearhProducts("", cat.getCatId());
		int n2 = productService.getNumberOfSearhProducts("", null);
		assertEquals(1,n);
		assertEquals(2,n2);
	}	
	
	//Obtener el numero de productos de una busqueda con categoria incorrecta
	//PR-UN-048
	@Test(expected = InstanceNotFoundException.class)
	public void getNumberOfSearhProductsInvalidTest() throws InstanceNotFoundException{

		Long l = Long.valueOf(5);
				
		when(catDao.find(l)).thenThrow(new InstanceNotFoundException("category", Category.class.getName()));
		productService.getNumberOfSearhProducts("", l);
		
		
	}	


}
