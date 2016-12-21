package es.udc.pa.pa007.auctionhouse.web.util;


/**
 * UserSession.
 *
 */
public class UserSession {

	/**
	 * The userProfile Id.
	 */
	private Long userProfileId;
	/**
	 * The fist name.
	 */
	private String firstName;

	/**
	 * @return the userProfile Id.
	 */
	public Long getUserProfileId() {
		return userProfileId;
	}

	/**
	 * @param userProfileId the userProfile Id.
	 */
	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}

	/**
	 * @return the first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the fist name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

}
