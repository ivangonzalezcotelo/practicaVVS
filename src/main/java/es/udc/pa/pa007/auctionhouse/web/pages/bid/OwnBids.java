package es.udc.pa.pa007.auctionhouse.web.pages.bid;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pa.pa007.auctionhouse.model.bidservice.BidService;
import es.udc.pa.pa007.auctionhouse.web.util.BidGridDataSource;
import es.udc.pa.pa007.auctionhouse.web.util.UserSession;

/**
 * OwnBids.
 *
 */
public class OwnBids {

	/**
	 * The Rows per page.
	 */
	private final int rowsPerPage = 4;

	/**
	 * The user Id.
	 */
	private Long userId;
	/**
	 * The BidGridDataSource.
	 */
	private BidGridDataSource bidGridDataSource;
	/**
	 * The Bid.
	 */
	private Bid bid;

	/**
	 * The product name.
	 */
	@Property
	private String prodName;

	/**
	 * The current winner.
	 */
	@Property
	private String actualWin;

	/**
	 * The Messages.
	 */
	@Inject
	private Messages messages;

	/**
	 * The UserSession.
	 */
	@SessionState(create = false)
	private UserSession userSession;

	/**
	 * The BidService.
	 */
	@Inject
	private BidService bidService;

	/**
	 * The Locale.
	 */
	@Inject
	private Locale locale;

	/**
	 * @return the Format.
	 */
	public Format getFormat() {
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		return formatter;
	}

	/**
	 * @param userId
	 *            the user Id.
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the BidGridDataSource.
	 */
	public BidGridDataSource getBidGridDataSource() {
		return bidGridDataSource;
	}

	/**
	 * @return the Bid.
	 */
	public Bid getBid() {
		return bid;
	}

	/**
	 * @param bid
	 *            the Bid.
	 */
	public void setBid(Bid bid) {
		this.bid = bid;
		if (bid.getActualWin() != null) {
			this.actualWin = bid.getActualWin().getLoginName();
		} else {
			this.actualWin = bid.getUserId().getLoginName();
		}
	}

	/**
	 * @return the number of rows per page.
	 */
	public int getRowsPerPage() {
		return rowsPerPage;
	}

	/**
	 * onActivate.
	 */
	public void onActivate() {
		this.userId = userSession.getUserProfileId();
		bidGridDataSource = new BidGridDataSource(bidService, userId);
	}

	/**
	 * @return the object.
	 */
	public Object[] onPassivate() {
		return new Object[] { userId };
	}

}
