package es.udc.pa.pa007.auctionhouse.model.bidservice;

/**
 * InvalidBidException.
 *
 */
@SuppressWarnings("serial")
public class InvalidBidException extends Exception {

	/**
	 * The reason.
	 */
	private String reason;

	/**
	 * @param reason
	 *            the reason.
	 */
	public InvalidBidException(String reason) {
		super("The bid has failed, the reason is => " + reason);
		this.reason = reason;
	}

	/**
	 * @return the reason.
	 */
	public String getReason() {
		return reason;
	}

}