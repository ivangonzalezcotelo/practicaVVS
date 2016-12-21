package es.udc.pa.pa007.auctionhouse.model.userservice;

import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * UserService.
 *
 */
public interface UserService {

	/**
	 * @param loginName
	 *            the login name.
	 * @param clearPassword
	 *            the clear password.
	 * @param userProfileDetails
	 *            the userProfile details.
	 * @return the UserProfile.
	 * @throws DuplicateInstanceException
	 *             if there exist another UserProfile with that login name.
	 */
	UserProfile registerUser(String loginName, String clearPassword, UserProfileDetails userProfileDetails)
			throws DuplicateInstanceException;

	/**
	 * @param loginName
	 *            the login name.
	 * @param password
	 *            the password.
	 * @param passwordIsEncrypted
	 *            the password encrypted.
	 * @return the UserProfile.
	 * @throws InstanceNotFoundException
	 *             if no exist UserProfile with that login name.
	 * @throws IncorrectPasswordException
	 *             if password is incorrect.
	 */
	UserProfile login(String loginName, String password, boolean passwordIsEncrypted)
			throws InstanceNotFoundException, IncorrectPasswordException;

	/**
	 * @param userProfileId
	 *            the userProfile Id.
	 * @return the UserProfile.
	 * @throws InstanceNotFoundException
	 *             if UserProfile is invalid.
	 */
	UserProfile findUserProfile(Long userProfileId) throws InstanceNotFoundException;

	/**
	 * @param userProfileId
	 *            the userProfile Id.
	 * @param userProfileDetails
	 *            the userProfile details.
	 * @throws InstanceNotFoundException
	 *             if UserProfile is invalid.
	 */
	void updateUserProfileDetails(Long userProfileId, UserProfileDetails userProfileDetails)
			throws InstanceNotFoundException;

	/**
	 * @param userProfileId
	 *            the userProfile Id.
	 * @param oldClearPassword
	 *            the old clear password.
	 * @param newClearPassword
	 *            the new clear password.
	 * @throws IncorrectPasswordException
	 *             if the password is incorrect.
	 * @throws InstanceNotFoundException
	 *             if not exist a UserProfile with that userProfileId.
	 */
	void changePassword(Long userProfileId, String oldClearPassword, String newClearPassword)
			throws IncorrectPasswordException, InstanceNotFoundException;

}
