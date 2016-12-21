package es.udc.pa.pa007.auctionhouse.model.bid;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

/**
 * Implementation.
 *
 */
@Repository("BidDao")
public class BidDaoHibernate extends GenericDaoHibernate<Bid, Long> implements BidDao {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Bid> findByUser(Long userId, int startIndex, int count) {

		return getSession()
				.createQuery("SELECT u FROM Bid u WHERE u.userId.userProfileId = :userId" + " ORDER BY u.bidDate DESC")
				.setParameter("userId", userId).setFirstResult(startIndex).setMaxResults(count).list();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getNumberOfBidsByUserId(Long userId) {

		long numberOfBids = (Long) getSession()
				.createQuery("SELECT COUNT(o) FROM Bid o WHERE " + "o.userId.userProfileId = :userId")
				.setParameter("userId", userId).uniqueResult();

		return (int) numberOfBids;

	}
}
