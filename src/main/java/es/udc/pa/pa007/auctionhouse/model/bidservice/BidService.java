package es.udc.pa.pa007.auctionhouse.model.bidservice;

import java.math.BigDecimal;
import java.util.List;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface BidService {

    public Bid makeBid(Long userId, Long prodId, BigDecimal maxPrice)
            throws InvalidBidException, InstanceNotFoundException;

    public List<Bid> listBids(Long userId, int startIndex, int count) throws InstanceNotFoundException;
    
    public int getNumberOfBidsByUserId(Long userId) throws InstanceNotFoundException;

}