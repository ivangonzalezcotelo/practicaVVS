package es.udc.pa.pa007.auctionhouse.model.bidservice;

@SuppressWarnings("serial")
public class InvalidBidException extends Exception {

    private String reason;
    
    public InvalidBidException(String reason) {
        super("The bid has failed, the reason is => " + reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }


}