package es.udc.pa.pa007.auctionhouse.model.bid;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

/**
 * The BidDao.
 *
 */
public interface BidDao extends GenericDao<Bid, Long> {

	/**
	 * Returns a Bid by an UserProfile.
	 * 
	 * @param userId
	 *            the UserProfile of the seller.
	 * @param startIndex
	 *            Start index of pagination.
	 * @param count
	 *            number of elements.
	 * @return the bid list<Bid> of the given user.
	 */
	List<Bid> findByUser(Long userId, int startIndex, int count);

	/**
	 * Returns number of Bids by an UserProfile.
	 *
	 * @param userId
	 *            the UserProfile of the seller.
	 * @return the number of bids of the given user.
	 */
	int getNumberOfBidsByUserId(Long userId);

}
