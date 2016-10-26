package es.udc.pa.pa007.auctionhouse.test.model.bidDao;

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
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class BidDaoTest {

	private Long bidderId;
	
	private Long getBidderId() {
		return bidderId;
	}

	private void setBidderId(Long bidderId) {
		this.bidderId = bidderId;
	}

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
		
		UserProfile owner = new UserProfile("owner", "PASS", "Peter", "Morrison", "peter@gmail.com");
		userDao.save(owner);
		
		UserProfile bidder = new UserProfile("bidder", "PASS", "John", "Carpenter", "john@gmail.com");
		userDao.save(bidder);
		setBidderId(bidder.getUserProfileId());
		
		Category cat = new Category("Musica");
		categoryDao.save(cat);
		
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		Calendar finish = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"));
		finish.add(Calendar.MINUTE, 50);
		
		Product product = new Product("CD","Beatles","Correos", BigDecimal.valueOf(7.50),now,finish,owner,cat);
		productDao.save(product);
		
		Bid bid = new Bid(product, bidder, BigDecimal.valueOf(7.50), BigDecimal.valueOf(13.50), Calendar.getInstance(),bidder);
		bidDao.save(bid);
		
	}
	
	/* Test de recuperación de lsita de apuestas para usuario existente */
	
	@Test
	public void testFindByUser() {
		
		List <Bid> bids = bidDao.findByUser(getBidderId(), 0, 10);
		
		assertEquals (1, bids.size());
	}
	
	/* Test de recuperación de número de apuestas para usuario existente */
	
	@Test
	public void testCountBids() 
			throws InstanceNotFoundException{
		
		int numBids = bidDao.getNumberOfBidsByUserId(getBidderId());
		
		assertEquals (1, numBids);
	}
	
}
