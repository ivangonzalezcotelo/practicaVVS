package es.udc.pa.pa007.auctionhouse.web.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserProfileDetails;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserService;
import es.udc.pa.pa007.auctionhouse.web.pages.Index;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicy;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicyType;
import es.udc.pa.pa007.auctionhouse.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;

/**
 * Register.
 *
 */
@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Register {

	/**
	 * The login name.
	 */
	@Property
	private String loginName;

	/**
	 * The password.
	 */
	@Property
	private String password;

	/**
	 * The retype password.
	 */
	@Property
	private String retypePassword;

	/**
	 * The first name.
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
	 * The registrationForm.
	 */
	@Component
	private Form registrationForm;

	/**
	 * The login name TextField.
	 */
	@Component(id = "loginName")
	private TextField loginNameField;

	/**
	 * The PasswordField.
	 */
	@Component(id = "password")
	private PasswordField passwordField;

	/**
	 * The Messages.
	 */
	@Inject
	private Messages messages;

	/**
	 * The UserProfile Id.
	 */
	private Long userProfileId;

	/**
	 * The onValidateFromRegistrationForm.
	 */
	public void onValidateFromRegistrationForm() {

		if (!registrationForm.isValid()) {
			return;
		}

		if (!password.equals(retypePassword)) {
			registrationForm.recordError(passwordField, messages.get("error-passwordsDontMatch"));
		} else {

			try {
				UserProfile userProfile = userService.registerUser(loginName, password,
						new UserProfileDetails(firstName, lastName, email));
				userProfileId = userProfile.getUserProfileId();
			} catch (DuplicateInstanceException e) {
				registrationForm.recordError(loginNameField, messages.get("error-loginNameAlreadyExists"));
			}

		}

	}

	/**
	 * The onSuccess.
	 * 
	 * @return The index.
	 */
	public Object onSuccess() {

		userSession = new UserSession();
		userSession.setUserProfileId(userProfileId);
		userSession.setFirstName(firstName);
		return Index.class;

	}

}
