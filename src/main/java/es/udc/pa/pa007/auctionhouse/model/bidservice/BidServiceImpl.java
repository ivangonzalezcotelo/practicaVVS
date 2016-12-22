package es.udc.pa.pa007.auctionhouse.model.bidservice;

//import org.hibernate.StaleObjectStateException;
//import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pa.pa007.auctionhouse.model.bid.BidDao;
import es.udc.pa.pa007.auctionhouse.model.product.ProductDao;
import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * Implementation.
 *
 */
@Service("bidservice")
@Transactional
public class BidServiceImpl implements BidService {

	/**
	 * The increase.
	 */
	private static final BigDecimal INCREMENTO = new BigDecimal(0.5);

	/**
	 * The BidDao.
	 */
	@Autowired
	private BidDao bidDao;

	/**
	 * The ProductDao.
	 */
	@Autowired
	private ProductDao productDao;

	/**
	 * The UserProfileDao.
	 */
	@Autowired
	private UserProfileDao userProfileDao;

	/**
	 * {@inheritDoc}
	 */
	public Bid makeBid(Long userId, Long prodId, BigDecimal maxPrice)
			throws InvalidBidException, InstanceNotFoundException {
		BigDecimal actualPrice = null;
		UserProfile actualWinner = new UserProfile();
		Boolean changeWinner = false;
		Product product = productDao.find(prodId);
		UserProfile user = userProfileDao.find(userId);

		// Si la fecha de subasta ya vencio
		if (product.getFinishDate().before(Calendar.getInstance())) {
			throw new InvalidBidException("product-out-of-date");
		}

		// Si el pujador es el mismo que el dueño del producto
		if (product.getOwner().getUserProfileId().equals(userId)) {
			throw new InvalidBidException("user-owner-product");
		}

		// Primera puja por el producto
		if (product.getActualPrice() == null) {
			// Si la puja es inferior al precio actual
			if (product.getLaunchPrice().compareTo(maxPrice) > 0) {
				throw new InvalidBidException("bid-must-be-greater");
			}
			actualPrice = product.getLaunchPrice();
			actualWinner = userProfileDao.find(userId);
			changeWinner = true;

		}

		if (product.getActualPrice() != null) {
			// Si la puja es inferior al precio actual
			if (product.getActualPrice().compareTo(maxPrice) >= 0) {
				throw new InvalidBidException("bid-must-be-greater");
			}

			// Si la puja es mayor, igual o menor a la puja máxima actual
			if (product.getWinnerBid().getMaxBid().compareTo(maxPrice) == -1) {
				actualPrice = product.getWinnerBid().getMaxBid().add(INCREMENTO);
				actualWinner = userProfileDao.find(userId);
				changeWinner = true;
			} else if (product.getWinnerBid().getMaxBid().compareTo(maxPrice) == 0) {
				actualPrice = product.getWinnerBid().getMaxBid();
				actualWinner = product.getWinnerBid().getUserId();
			} else {
				actualPrice = maxPrice.add(INCREMENTO);
				actualWinner = product.getWinnerBid().getUserId();
			}
		}

		product.setActualPrice(actualPrice);
		Bid bid = new Bid(product, user, actualPrice, maxPrice, Calendar.getInstance(), actualWinner);
		if (changeWinner) {
			product.setWinnerBid(bid);
		}
		bidDao.save(bid);
		return bid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public List<Bid> listBids(Long userId, int startIndex, int count) throws InstanceNotFoundException {

		return bidDao.findByUser(userProfileDao.find(userId).getUserProfileId(), startIndex, count);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = true)
	public int getNumberOfBidsByUserId(Long userId) throws InstanceNotFoundException {

		/* Check if account exists. */
		userProfileDao.find(userId);

		/* Return count. */
		return bidDao.getNumberOfBidsByUserId(userId);

	}
}
