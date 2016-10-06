package es.udc.pa.pa007.auctionhouse.test.model.bidservice;

import static es.udc.pa.pa007.auctionhouse.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa007.auctionhouse.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pa.pa007.auctionhouse.model.bidservice.BidService;
import es.udc.pa.pa007.auctionhouse.model.bidservice.InvalidBidException;
import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.category.CategoryDao;
import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserProfileDetails;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class BidServiceTest {

	@Autowired
	private BidService bidService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Test
	public void testNoListBids() 
			throws DuplicateInstanceException, InstanceNotFoundException{
		UserProfile user = userService.registerUser(
				"user", "userPassword", new UserProfileDetails("name", "lastName", "user@udc.es"));

		List <Bid> bids = bidService.listBids(user.getUserProfileId(), 0, 10);
		
		assertEquals (0, bids.size());
	}
	
	@Test
	public void testFirstMakeBid() 
			throws DuplicateInstanceException, InvalidBidException, InstanceNotFoundException{
		UserProfile user = userService.registerUser(
				"user", "userPassword", new UserProfileDetails("user", "lastName", "user@udc.es"));		
		
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));		

		Category camaras = new Category("Camaras Fotográficas");
		categoryDao.save(camaras);
		
		Product product = productService.insertProduct("Canon S50", "Camara de fotos", 
				new BigDecimal(10), "Informacion", 4, owner.getUserProfileId(), camaras.getCatId());
				
		Bid bid = bidService.makeBid(user.getUserProfileId(), product.getProdId(), new BigDecimal(12));
		
		List<Bid> bids = bidService.listBids(user.getUserProfileId(), 0, 10);
		Product search = productService.findByProductId(product.getProdId());
		
		assertEquals( 1, bids.size());
		assertEquals( bid.getProductId().getProdId(), bids.get(0).getProductId().getProdId());
		assertEquals( bid.getMaxBid(), bids.get(0).getMaxBid());
		assertEquals( user.getUserProfileId(), bids.get(0).getActualWin().getUserProfileId());
		assertEquals( new BigDecimal(10), bids.get(0).getCurrentValue());
		assertEquals( search.getWinnerBid().getBidId(), bids.get(0).getBidId());
	};
	
	@Test(expected = InvalidBidException.class)
	public void testOwnerMakeBidOwner() 
			throws InvalidBidException, DuplicateInstanceException, InstanceNotFoundException{
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));		

		Category cat = new Category("Prueba");
		categoryDao.save(cat);
		
		Product p = productService.insertProduct("Prueba", "Objeto de prueba", new BigDecimal(10), 
				"Informacion", 4, owner.getUserProfileId(), cat.getCatId());
		
		bidService.makeBid(owner.getUserProfileId(), p.getProdId(), new BigDecimal(20));
	}
	
	@Test(expected = InvalidBidException.class)
	public void testMakeBidInsuficient() 
			throws DuplicateInstanceException, InstanceNotFoundException, InvalidBidException{
		UserProfile user = userService.registerUser(
				"user", "userPassword", new UserProfileDetails("user", "lastName", "user@udc.es"));		
		
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));		

		Category cat = new Category("Prueba");
		categoryDao.save(cat);
		
		Product p = productService.insertProduct("Prueba", "Objeto de prueba", new BigDecimal(10), 
				"Informacion", 4, owner.getUserProfileId(), cat.getCatId());
		bidService.makeBid(user.getUserProfileId(), p.getProdId(), new BigDecimal(5.5));
	};
	
	@Test
	public void testTwoMakeBids() 
			throws DuplicateInstanceException, InstanceNotFoundException, InvalidBidException{
		UserProfile compradorA = userService.registerUser(
				"compradorA", "userPassword", new UserProfileDetails("compradorA", "lastName", "user@udc.es"));		

		UserProfile compradorB = userService.registerUser(
				"compradorB", "userPassword", new UserProfileDetails("compradorB", "lastName", "user@udc.es"));		
		
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));		

		Category cat = new Category("Prueba");
		categoryDao.save(cat);
		
		Product p = productService.insertProduct("Prueba", "Objeto de prueba", new BigDecimal(10), 
				"Informacion", 4, owner.getUserProfileId(), cat.getCatId());
		
		Bid bid = bidService.makeBid(compradorA.getUserProfileId(), p.getProdId(), new BigDecimal(12));

		bidService.makeBid(compradorB.getUserProfileId(), p.getProdId(), new BigDecimal(11));
		List <Bid> bidsA = bidService.listBids(compradorA.getUserProfileId(), 0, 10);
		List <Bid> bidsB = bidService.listBids(compradorB.getUserProfileId(), 0, 10);
		Product product = productService.findByProductId(p.getProdId());
		
		assertEquals(bid.getBidId(), product.getWinnerBid().getBidId());
		assertEquals(compradorA.getUserProfileId(), product.getWinnerBid().getActualWin().getUserProfileId());
		assertEquals(new BigDecimal(11.5), product.getActualPrice());
		assertEquals(1, bidsB.size());
		assertEquals(1, bidsA.size());
	}
	
	@Test
	public void testTwoMakeBids2() 
			throws DuplicateInstanceException, InstanceNotFoundException, InvalidBidException{
		UserProfile compradorA = userService.registerUser(
				"compradorA", "userPassword", new UserProfileDetails("compradorA", "lastName", "user@udc.es"));		

		UserProfile compradorB = userService.registerUser(
				"compradorB", "userPassword", new UserProfileDetails("compradorB", "lastName", "user@udc.es"));		
		
		UserProfile owner = userService.registerUser(
				"owner", "ownerPassword", new UserProfileDetails("owner", "lastName", "owner@udc.es"));		

		Category cat = new Category("Prueba");
		categoryDao.save(cat);
		
		Product p = productService.insertProduct("Prueba", "Objeto de prueba", new BigDecimal(10), 
				"Informacion", 4, owner.getUserProfileId(), cat.getCatId());
		
		bidService.makeBid(compradorA.getUserProfileId(), p.getProdId(), new BigDecimal(12));

		Bid bid = bidService.makeBid(compradorB.getUserProfileId(), p.getProdId(), new BigDecimal(14));
		List <Bid> bids = bidService.listBids(compradorB.getUserProfileId(), 0, 10);
		Product product = productService.findByProductId(p.getProdId());
		
		assertEquals(bid.getBidId(), product.getWinnerBid().getBidId());
		assertEquals(compradorB.getUserProfileId(), product.getWinnerBid().getActualWin().getUserProfileId());
		assertEquals(new BigDecimal(12.5), product.getActualPrice());
		assertEquals(1, bids.size());
	}
}
