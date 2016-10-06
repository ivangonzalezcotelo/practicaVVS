package es.udc.pa.pa007.auctionhouse.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pa.pa007.auctionhouse.model.bidservice.BidService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class BidGridDataSource implements GridDataSource {

	private BidService bidService; 
	private Long userId;
	private List<Bid> bids;
	private int startIndex;
	private boolean userNotFound;

	public BidGridDataSource(BidService bidService,Long userId) {
		
		this.bidService = bidService;
		this.userId = userId;

	}

    public int getAvailableRows() {
    	    	
    	try {		
			return bidService.getNumberOfBidsByUserId(userId);
		} catch (InstanceNotFoundException e) {
			userNotFound = true;
			return 0;
		}
        
    }

    public Class<Bid> getRowType() {
        return Bid.class;
    }

    public Object getRowValue(int index) {
        return bids.get(index-this.startIndex);
    }

    public void prepare(int startIndex, int endIndex,
    	List<SortConstraint> sortConstraints) {

        try {
        	
        	bids = bidService.listBids(userId, startIndex, endIndex-startIndex+1);
	        this.startIndex = startIndex;

		} catch (InstanceNotFoundException e) {
		}

    }

    public boolean getUserNotFound() {
    	return userNotFound;
    }

}
