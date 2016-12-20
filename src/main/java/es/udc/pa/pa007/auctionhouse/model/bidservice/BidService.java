package es.udc.pa.pa007.auctionhouse.model.bidservice;

import java.math.BigDecimal;
import java.util.List;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * BidService.
 *
 */
public interface BidService {

    /**
     * Make a bid.
     * 
     * @param userId the userId.
     * @param prodId the prodId.
     * @param maxPrice the maxPrice.
     * @return the Bid.
     * @throws InvalidBidException if the bid has invalid parameters.
     * @throws InstanceNotFoundException if the user or product are invalid.
     */
    Bid makeBid(Long userId, Long prodId, BigDecimal maxPrice)
            throws InvalidBidException, InstanceNotFoundException;

    /**
     * List the bids of an User.
     * 
     * @param userId the userId.
     * @param startIndex the start position.
     * @param count number of elements.
     * @return the list of Bids.
     * @throws InstanceNotFoundException if the user is invalid.
     */
    List<Bid> listBids(Long userId, int startIndex, int count) throws InstanceNotFoundException;
    
    /**
     * Number of user bids.
     * 
     * @param userId the userId.
     * @return the number of bids.
     * @throws InstanceNotFoundException if the user is invalid.
     */
    int getNumberOfBidsByUserId(Long userId) throws InstanceNotFoundException;

}