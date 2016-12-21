package es.udc.pa.pa007.auctionhouse.model.userprofile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;
import static es.udc.pa.pa007.auctionhouse.model.util.GlobalNames.PAGINATION;

/**
 * UserProfile.
 *
 */
@Entity
@BatchSize(size = PAGINATION)
public class UserProfile {

	/**
	 * The userProfile Id.
	 */
	private Long userProfileId;
	/**
	 * The login name.
	 */
	private String loginName;
	/**
	 * The encrypted password.
	 */
	private String encryptedPassword;
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
	 * Instance.
	 */
	public UserProfile() {
	}

	/**
	 * @param loginName
	 *            the login name.
	 * @param encryptedPassword
	 *            the encrypted password.
	 * @param firstName
	 *            the first name.
	 * @param lastName
	 *            the last name.
	 * @param email
	 *            the email.
	 */
	public UserProfile(String loginName, String encryptedPassword, String firstName, String lastName, String email) {

		/**
		 * NOTE: "userProfileId" *must* be left as "null" since its value is
		 * automatically generated.
		 */

		this.loginName = loginName;
		this.encryptedPassword = encryptedPassword;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	/**
	 * @return the userProfile Id.
	 */
	@Column(name = "usrId")
	@SequenceGenerator(// It only takes effect for
			name = "UserProfileIdGenerator", // databases providing identifier
			sequenceName = "UserProfileSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "UserProfileIdGenerator")
	public Long getUserProfileId() {
		return userProfileId;
	}

	/**
	 * @param userProfileId
	 *            the userProfile Id.
	 */
	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}

	/**
	 * @return the login name.
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginName
	 *            the login name.
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return the encrypted password.
	 */
	@Column(name = "enPassword")
	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	/**
	 * @param encryptedPassword
	 *            the encrypted password.
	 */
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	/**
	 * @return the first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "UserProfile [userProfileId=" + userProfileId + ", loginName=" + loginName + ", encryptedPassword="
				+ encryptedPassword + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}

}
