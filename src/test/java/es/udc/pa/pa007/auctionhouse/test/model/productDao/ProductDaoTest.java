package es.udc.pa.pa007.auctionhouse.test.model.productDao;

import static es.udc.pa.pa007.auctionhouse.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa007.auctionhouse.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pa.pa007.auctionhouse.model.bid.BidDao;
import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.category.CategoryDao;
import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.product.ProductDao;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfileDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class ProductDaoTest {

		private UserProfile bidder;
		
		private UserProfile owner;
		
		private Category cat1;
		
		private Category cat2;
		
		private Product p1;
		
		private Product p2;
		
		private Product p3;
		
		private Product p4;
		
		@Autowired
		private  BidDao bidDao;
		
		@Autowired
		private  CategoryDao categoryDao;
		
		@Autowired
		private  UserProfileDao userDao;
		
		@Autowired
		private  ProductDao productDao;
		
		@Before
		public void initialize(){
			
			owner = new UserProfile("owner", "PASS", "Peter", "Morrison", "peter@gmail.com");
			userDao.save(owner);
			
			bidder = new UserProfile("bidder", "PASS", "John", "Carpenter", "john@gmail.com");
			userDao.save(bidder);
			
			cat1 = new Category("Musica");
			categoryDao.save(cat1);
			
			cat2 = new Category("Deportes");
			categoryDao.save(cat2);
			
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
			Calendar finish = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
			finish.add(Calendar.MINUTE, 50);
			
			p1 = new Product("CD1","Beatles","Correos", BigDecimal.valueOf(7.50),now,finish,owner,cat1);
			productDao.save(p1);
			
			p2 = new Product("CD2","Rollings","Correos", BigDecimal.valueOf(9.50),now,finish,owner,cat1);
			productDao.save(p2);
			
			p3 = new Product("Vinilo","DireStraits","Correos", BigDecimal.valueOf(16.50),now,finish,owner,cat1);
			productDao.save(p3);
			
			p4 = new Product("Raqueta","Wilson","Correos", BigDecimal.valueOf(67.50),now,finish,owner,cat2);
			productDao.save(p4);
			
			Bid bid = new Bid(p1, bidder, BigDecimal.valueOf(7.50), BigDecimal.valueOf(13.50), Calendar.getInstance(),bidder);
			bidDao.save(bid);
			
		}
		
	
	@Test
	public void findByKeyAndCategoryTest(){
		
		String keyword;
		List<Product> list;
		
		//Test con categoria nula y keyword vacía
		
		list = productDao.findByKeyAndCategory("", null, 0, 5);
		assertEquals (4, list.size());
		
		//Test con keyword determinada y categoría nula
		
		keyword = "CD";
		list = productDao.findByKeyAndCategory(keyword, null, 0, 5);
		assertEquals (2, list.size());
	
		//Test con categoría determinada y keyword vacía
	
		list = productDao.findByKeyAndCategory("", cat1.getCatId(), 0, 5);
		assertEquals (3, list.size());
		
		//Test con categoría y keyword determinada
		
		keyword = "Raq";
		list = productDao.findByKeyAndCategory(keyword, cat2.getCatId(), 0, 5);
		assertEquals (1, list.size());
		
	}

	@Test
	public void findByOwnerTest(){
	
		List<Product> list;	
		
		//Test con usuario existente
		
		list = productDao.findByOwner(owner.getUserProfileId(), 0, 10);
		assertEquals(4,list.size());
		
		//Test con usuario no existente

		list = productDao.findByOwner(null, 0, 10);
		assertEquals(0,list.size());
	}

	@Test
	public void getNumberOfProductsTest(){
		
		int pNumber;
		
		//Test con usuario existente
		
		pNumber = productDao.getNumberOfProducts(owner.getUserProfileId());
		assertEquals(4,pNumber);
		
		//Test con usuario no existente
		
		pNumber = productDao.getNumberOfProducts(null);
		assertEquals(0,pNumber);
		
	}

	
	@Test
	public void getNumberOfSearchProductsTest(){
		
		String keyword;
		int coincidences;
		
		//Test con categoria nula y keyword vacía
		
		coincidences = productDao.getNumberOfSearchProducts("", null);
		assertEquals (4, coincidences);
		
		//Test con keyword determinada y categoría nula
		
		keyword = "CD";
		coincidences = productDao.getNumberOfSearchProducts(keyword, null);
		assertEquals (2, coincidences);
	
		//Test con categoría determinada y keyword vacía
	
		coincidences = productDao.getNumberOfSearchProducts("", cat1.getCatId());
		assertEquals (3, coincidences);
		
		//Test con categoría y keyword determinada
		
		keyword = "Raq";
		coincidences = productDao.getNumberOfSearchProducts(keyword, cat2.getCatId());
		assertEquals (1, coincidences);
		
	}
	
	
	
}
