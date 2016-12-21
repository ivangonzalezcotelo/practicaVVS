package es.udc.pa.pa007.auctionhouse.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pa.pa007.auctionhouse.model.bidservice.BidService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * BidGridDataSource.
 *
 */
public class BidGridDataSource implements GridDataSource {

	/**
	 * The BidService.
	 */
	private BidService bidService;
	/**
	 * The userProfile Id.
	 */
	private Long userId;
	/**
	 * The bids.
	 */
	private List<Bid> bids;
	/**
	 * The start index.
	 */
	private int startIndex;
	/**
	 * The boolean userNotFound.
	 */
	private boolean userNotFound;

	/**
	 * @param bidService
	 *            the BidService.
	 * @param userId
	 *            the userProfile Id.
	 */
	public BidGridDataSource(BidService bidService, Long userId) {

		this.bidService = bidService;
		this.userId = userId;

	}

	/**
	 * @return the available rows.
	 */
	public int getAvailableRows() {

		try {
			return bidService.getNumberOfBidsByUserId(userId);
		} catch (InstanceNotFoundException e) {
			userNotFound = true;
			return 0;
		}

	}

	/**
	 * @return the row type.
	 */
	public Class<Bid> getRowType() {
		return Bid.class;
	}

	/**
	 * @param index
	 *            the index.
	 * @return the row value.
	 */
	public Object getRowValue(int index) {
		return bids.get(index - this.startIndex);
	}

	/**
	 * @param startIndex
	 *            the start index.
	 * @param endIndex
	 *            the end index.
	 * @param sortConstraints
	 *            the sortConstraints.
	 */
	public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {

		try {

			bids = bidService.listBids(userId, startIndex, endIndex - startIndex + 1);
			this.startIndex = startIndex;

		} catch (InstanceNotFoundException e) {
		}

	}

	/**
	 * @return the boolean userNotFound.
	 */
	public boolean getUserNotFound() {
		return userNotFound;
	}

}
