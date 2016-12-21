package es.udc.pa.pa007.auctionhouse.model.userservice;

/**
 * UserProfileDetails.
 *
 */
public class UserProfileDetails {

	/**
	 * The first name.
	 */
	private String firstName;
	/**
	 * The last name.
	 */
	private String lastName;
	/**
	 * The email.
	 */
	private String email;

	/**
	 * @param firstName
	 *            the fist name.
	 * @param lastName
	 *            the last name.
	 * @param email
	 *            the email.
	 */
	public UserProfileDetails(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	/**
	 * @return the fist name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the email.
	 */
	public String getEmail() {
		return email;
	}

}
