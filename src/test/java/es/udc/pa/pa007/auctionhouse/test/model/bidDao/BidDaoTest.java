package es.udc.pa.pa007.auctionhouse.test.model.bidDao;

import static es.udc.pa.pa007.auctionhouse.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa007.auctionhouse.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.util.List;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pa.pa007.auctionhouse.model.bid.BidDao;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class BidDaoTest {

	@Autowired
	private  BidDao bidDao;
	
	@Test
	public void testFindUser() 
			throws InstanceNotFoundException{
		
		List <Bid> bids = bidDao.findByUser(Long.valueOf(4), 0, 10);
		
		assertEquals (1, bids.size());
	}	

	@Test
	public void testCountBids() 
			throws InstanceNotFoundException{
		
		int numBids = bidDao.getNumberOfBidsByUserId(Long.valueOf(5));
		
		assertEquals (2, numBids);
	}	
	
}
