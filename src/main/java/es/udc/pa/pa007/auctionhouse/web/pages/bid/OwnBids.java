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

public class OwnBids {

	private final static int ROWS_PER_PAGE = 4;

	private Long userId;
	private BidGridDataSource bidGridDataSource;
	private Bid bid;

	@Property
	private String prodName;

	@Property
	private String actualWin;

	@Inject
	private Messages messages;

	@SessionState(create = false)
	private UserSession userSession;

	@Inject
	private BidService bidService;

	@Inject
	private Locale locale;

	public Format getFormat() {
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		return formatter;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BidGridDataSource getBidGridDataSource() {
		return bidGridDataSource;
	}

	public Bid getBid() {
		return bid;
	}

	public void setBid(Bid bid) {
		this.bid = bid;
		if (bid.getActualWin() != null) {
			this.actualWin = bid.getActualWin().getLoginName();
		} else {
			this.actualWin = bid.getUserId().getLoginName();
		}
	}

	public int getRowsPerPage() {
		return ROWS_PER_PAGE;
	}

	void onActivate() {
		this.userId = userSession.getUserProfileId();
		bidGridDataSource = new BidGridDataSource(bidService, userId);
	}

	Object[] onPassivate() {
		return new Object[] { userId };
	}

}
