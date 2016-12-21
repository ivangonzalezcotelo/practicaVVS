package es.udc.pa.pa007.auctionhouse.web.pages.user;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserProfileDetails;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserService;
import es.udc.pa.pa007.auctionhouse.web.pages.Index;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicy;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicyType;
import es.udc.pa.pa007.auctionhouse.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * UpdateProfile.
 *
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class UpdateProfile {

	/**
	 * The fist name.
	 */
	@Property
	private String firstName;

	/**
	 * The last name.
	 */
	@Property
	private String lastName;

	/**
	 * The email.
	 */
	@Property
	private String email;

	/**
	 * The UserSession.
	 */
	@SessionState(create = false)
	private UserSession userSession;

	/**
	 * The UserService.
	 */
	@Inject
	private UserService userService;

	/**
	 * The onPrepareForRender.
	 * 
	 * @throws InstanceNotFoundException
	 *             if userProfile is invalid.
	 */
	public void onPrepareForRender() throws InstanceNotFoundException {

		UserProfile userProfile;

		userProfile = userService.findUserProfile(userSession.getUserProfileId());
		firstName = userProfile.getFirstName();
		lastName = userProfile.getLastName();
		email = userProfile.getEmail();

	}

	/**
	 * The onSuccess.
	 * 
	 * @return the Index.
	 * @throws InstanceNotFoundException
	 *             if userProfile is invalid.
	 */
	public Object onSuccess() throws InstanceNotFoundException {

		userService.updateUserProfileDetails(userSession.getUserProfileId(),
				new UserProfileDetails(firstName, lastName, email));
		userSession.setFirstName(firstName);
		return Index.class;

	}

}