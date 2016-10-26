package es.udc.pa.pa007.auctionhouse.test.model.bidservice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pa.pa007.auctionhouse.model.bid.BidDao;
import es.udc.pa.pa007.auctionhouse.model.bidservice.BidServiceImpl;
import es.udc.pa.pa007.auctionhouse.model.bidservice.InvalidBidException;
import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.product.ProductDao;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class BidServiceUnitTest {

	@InjectMocks
	private BidServiceImpl bidService = new BidServiceImpl();

	@Mock
	private BidDao bidDao;

	@Mock
	private ProductDao productDao;

	@Mock
	private UserProfileDao userDao;
	
	// Test de puja con fecha expirada
	/* PR-UN-023 */
	@Test(expected = InvalidBidException.class)
	public void makeBidExpiratedDateTest() throws InstanceNotFoundException,
			InvalidBidException {

		Category cat = new Category("Musica");
		UserProfile user1 = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");
		UserProfile user2 = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");

		Calendar now = Calendar.getInstance();
		Calendar finish = Calendar.getInstance();
		now.add(Calendar.MINUTE, -10);
		finish.add(Calendar.MINUTE, -5);
		Product p1 = new Product("CD", "Beatles", "Correos",
				BigDecimal.valueOf(7.50), now, finish, user1, cat);
		when(productDao.find(p1.getProdId())).thenReturn(p1);
		when(userDao.find(user2.getUserProfileId())).thenReturn(user2);
		bidService.makeBid(user2.getUserProfileId(), p1.getProdId(),
				BigDecimal.valueOf(20));
	}

	// Test de puja con bidder igual al owner
	/* PR-UN-024 */
	@Test(expected = InvalidBidException.class)
	public void makeBidSameUser() throws InstanceNotFoundException,
			InvalidBidException {

		Category cat = new Category("Musica");
		UserProfile user = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");
		user.setUserProfileId(Long.valueOf(1));
		Calendar now = Calendar.getInstance();
		Calendar finish = Calendar.getInstance();
		finish.add(Calendar.MINUTE, 5);
		Product p1 = new Product("CD", "Beatles", "Correos",
				BigDecimal.valueOf(7.50), now, finish, user, cat);
		when(productDao.find(p1.getProdId())).thenReturn(p1);
		when(userDao.find(user.getUserProfileId())).thenReturn(user);
		bidService.makeBid(user.getUserProfileId(), p1.getProdId(),
				BigDecimal.valueOf(20));
	}

	// Primera puja con precio inferior al actual
	/* PR-UN-025 */
	@Test(expected = InvalidBidException.class)
	public void makeBidLowerFirstBid() throws InstanceNotFoundException,
			InvalidBidException {

		Category cat = new Category("Musica");
		UserProfile user1 = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");
		UserProfile user2 = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");
		user1.setUserProfileId(Long.valueOf(1));
		user2.setUserProfileId(Long.valueOf(2));

		Calendar now = Calendar.getInstance();
		Calendar finish = Calendar.getInstance();
		finish.add(Calendar.MINUTE, 5);

		Product p1 = new Product("CD", "Beatles", "Correos",
				BigDecimal.valueOf(7.50), now, finish, user1, cat);

		when(productDao.find(p1.getProdId())).thenReturn(p1);
		when(userDao.find(user2.getUserProfileId())).thenReturn(user2);

		bidService.makeBid(user2.getUserProfileId(), p1.getProdId(),
				BigDecimal.valueOf(5));
	}

	// Test con primera puja normal y siguientes pujas normales 
	// (menor, igual o mayor)
	/* PR-UN-026 */
	@Test
	public void makeBidNormalBid() throws InstanceNotFoundException,
			InvalidBidException {

		Category cat = new Category("Musica");
		UserProfile user1 = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");
		UserProfile user2 = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");
		UserProfile user3 = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");
		user1.setUserProfileId(Long.valueOf(1));
		user2.setUserProfileId(Long.valueOf(2));
		user3.setUserProfileId(Long.valueOf(3));

		Calendar now = Calendar.getInstance();
		Calendar finish = Calendar.getInstance();
		finish.add(Calendar.MINUTE, 5);

		Product p1 = new Product("CD", "Beatles", "Correos",
				BigDecimal.valueOf(7.50), now, finish, user1, cat);

		when(productDao.find(p1.getProdId())).thenReturn(p1);
		when(userDao.find(user2.getUserProfileId())).thenReturn(user2);
		when(userDao.find(user3.getUserProfileId())).thenReturn(user3);

		Bid bid = bidService.makeBid(user2.getUserProfileId(), p1.getProdId(),
				BigDecimal.valueOf(12));

		assertEquals(bid.getActualWin(), user2);

		// Segunda puja inferior que el máximo

		bid = bidService.makeBid(user3.getUserProfileId(), p1.getProdId(),
				BigDecimal.valueOf(10));

		assertEquals(bid.getCurrentValue(), BigDecimal.valueOf(10.5));
		assertEquals(bid.getActualWin(), user2);

		// Tercera puja igual al máximo

		bid = bidService.makeBid(user3.getUserProfileId(), p1.getProdId(),
				BigDecimal.valueOf(12));

		assertEquals(bid.getCurrentValue(), BigDecimal.valueOf(12));
		assertEquals(bid.getActualWin(), user2);

		// Cuarta puja mayor que máximo

		bid = bidService.makeBid(user3.getUserProfileId(), p1.getProdId(),
				BigDecimal.valueOf(15));

		assertEquals(bid.getCurrentValue(), BigDecimal.valueOf(12.5));
		assertEquals(bid.getActualWin(), user3);

		verify(bidDao, times(4)).save(any(Bid.class));

	}

	// Test con segunda puja inferior
	/* PR-UN-027 */
	@Test(expected = InvalidBidException.class)
	public void makeBidLowerSecondBid() throws InstanceNotFoundException,
			InvalidBidException {

		Category cat = new Category("Musica");
		UserProfile user1 = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");
		UserProfile user2 = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");
		UserProfile user3 = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");
		user1.setUserProfileId(Long.valueOf(1));
		user2.setUserProfileId(Long.valueOf(2));
		user3.setUserProfileId(Long.valueOf(2));

		Calendar now = Calendar.getInstance();
		Calendar finish = Calendar.getInstance();
		finish.add(Calendar.MINUTE, 5);

		Product p1 = new Product("CD", "Beatles", "Correos",
				BigDecimal.valueOf(7.50), now, finish, user1, cat);

		when(productDao.find(p1.getProdId())).thenReturn(p1);
		when(userDao.find(user2.getUserProfileId())).thenReturn(user2);
		when(userDao.find(user3.getUserProfileId())).thenReturn(user3);

		bidService.makeBid(user2.getUserProfileId(), p1.getProdId(),
				BigDecimal.valueOf(23));

		verify(bidDao, times(1)).save(any(Bid.class));

		bidService.makeBid(user3.getUserProfileId(), p1.getProdId(),
				BigDecimal.valueOf(5));

	}
	
	// Test de listar bids de un usuario no existente
	/* PR-UN-028 */
	@Test(expected=InstanceNotFoundException.class)
	public void listsBidsTestUserNonExistent() throws InstanceNotFoundException{
		UserProfile user1 = new UserProfile("user", "pass", "Johann",
				"Petersen", "johann@gmail.com");
		user1.setUserProfileId(Long.valueOf(1));
		
		when(userDao.find(user1.getUserProfileId())).thenThrow(new InstanceNotFoundException("user", UserProfile.class.getName()));	
		bidService.listBids(user1.getUserProfileId(), 0, 10);
	}
	
	// Test de listar bids de un usuario existente
	/* PR-UN-029 */
		@Test
		public void listsBidsTest() throws InstanceNotFoundException{
			
			Category cat = new Category("Musica)");
			
			UserProfile user1 = new UserProfile("user", "pass", "Johann",
					"Petersen", "johann@gmail.com");
			user1.setUserProfileId(Long.valueOf(1));
			
			UserProfile user2 = new UserProfile("user", "pass", "Johann",
					"Petersen", "johann@gmail.com");
			user2.setUserProfileId(Long.valueOf(2));
			
			Calendar now = Calendar.getInstance();
			Calendar finish = Calendar.getInstance();
			finish.add(Calendar.MINUTE, 5);

			Product p1 = new Product("CD", "Beatles", "Correos",
					BigDecimal.valueOf(7.50), now, finish, user1, cat);
			
			Bid bid = new Bid(p1,user2,BigDecimal.valueOf(7.5),BigDecimal.valueOf(12),now,null);
			
			List<Bid> list1 = new ArrayList<Bid>();
			list1.add(bid);
			
			when(userDao.find(user2.getUserProfileId())).thenReturn(user2);
			when(bidDao.findByUser(user2.getUserProfileId(), 0, 10) ).thenReturn(list1);
			
			List<Bid> list2 = bidService.listBids(user2.getUserProfileId(), 0, 10);
			
			assertEquals(1,list2.size());
		}
		
		//Test de obtener el número de apuestas de un usuario no existente
		/* PR-UN-030 */
		@Test(expected=InstanceNotFoundException.class)
		public void getNumberOfBidsNonExistentUserTest() throws InstanceNotFoundException{
		
			UserProfile user1 = new UserProfile("user", "pass", "Johann",
					"Petersen", "johann@gmail.com");
			user1.setUserProfileId(Long.valueOf(1));
			
			when(userDao.find(user1.getUserProfileId())).thenThrow(new InstanceNotFoundException("user", UserProfile.class.getName()));	
			bidService.getNumberOfBidsByUserId(user1.getUserProfileId());
			
		}
		
		//Test de obtener el número de apuestas de un usuario existente
		/* PR-UN-031 */
		@Test
		public void getNumberOfBidsTest() throws InstanceNotFoundException{
		
			
			UserProfile user1 = new UserProfile("user", "pass", "Johann",
					"Petersen", "johann@gmail.com");
			user1.setUserProfileId(Long.valueOf(1));
			
			when(userDao.find(user1.getUserProfileId())).thenReturn(user1);
			when(bidDao.getNumberOfBidsByUserId(user1.getUserProfileId())).thenReturn(1);
			
			int nBids = bidService.getNumberOfBidsByUserId(user1.getUserProfileId());
			assertEquals(1, nBids);
		}
		
}
