package es.udc.pa.pa007.auctionhouse.web.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.pa.pa007.auctionhouse.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserService;
import es.udc.pa.pa007.auctionhouse.web.pages.Index;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicy;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicyType;
import es.udc.pa.pa007.auctionhouse.web.util.CookiesManager;
import es.udc.pa.pa007.auctionhouse.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * ChangePassword.
 *
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ChangePassword {

	/**
	 * The old password.
	 */
	@Property
	private String oldPassword;

	/**
	 * The new password.
	 */
	@Property
	private String newPassword;

	/**
	 * The retype new password.
	 */
	@Property
	private String retypeNewPassword;

	/**
	 * The UserSession.
	 */
	@SessionState(create = false)
	private UserSession userSession;

	/**
	 * The change password Form.
	 */
	@Component
	private Form changePasswordForm;

	/**
	 * The Cookies.
	 */
	@Inject
	private Cookies cookies;

	/**
	 * The Messages.
	 */
	@Inject
	private Messages messages;

	/**
	 * The UserService.
	 */
	@Inject
	private UserService userService;

	/**
	 * The onValidateFromChangePasswordForm.
	 * 
	 * @throws InstanceNotFoundException
	 *             if userProfile is invalid.
	 */
	public void onValidateFromChangePasswordForm() throws InstanceNotFoundException {

		if (!changePasswordForm.isValid()) {
			return;
		}

		if (!newPassword.equals(retypeNewPassword)) {
			changePasswordForm.recordError(messages.get("error-passwordsDontMatch"));
		} else {

			try {
				userService.changePassword(userSession.getUserProfileId(), oldPassword, newPassword);
			} catch (IncorrectPasswordException e) {
				changePasswordForm.recordError(messages.get("error-invalidPassword"));
			}

		}

	}

	/**
	 * @return the Index.
	 */
	public Object onSuccess() {

		CookiesManager.removeCookies(cookies);
		return Index.class;

	}

}
