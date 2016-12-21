package es.udc.pa.pa007.auctionhouse.web.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserService;
import es.udc.pa.pa007.auctionhouse.web.pages.Index;
import es.udc.pa.pa007.auctionhouse.web.pages.bid.MakeBid;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicy;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicyType;
import es.udc.pa.pa007.auctionhouse.web.util.CookiesManager;
import es.udc.pa.pa007.auctionhouse.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * Login.
 *
 */
@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Login {

	/**
	 * The login name.
	 */
	@Property
	private String loginName;

	/**
	 * the password.
	 */
	@Property
	private String password;

	/**
	 * The boolean rememberMyPassword.
	 */
	@Property
	private boolean rememberMyPassword;

	/**
	 * The UserSession.
	 */
	@SessionState(create = false)
	private UserSession userSession;

	/**
	 * The Cookies.
	 */
	@Inject
	private Cookies cookies;

	/**
	 * The login Form.
	 */
	@Component
	private Form loginForm;

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
	 * The UserProfile.
	 */
	private UserProfile userProfile = null;
	/**
	 * The product Id.
	 */
	private Long productId;

	/**
	 * The MakeBid.
	 */
	@InjectPage
	private MakeBid makeBid;

	/**
	 * The onValidateFromLoginForm.
	 */
	public void onValidateFromLoginForm() {

		if (!loginForm.isValid()) {
			return;
		}

		try {
			userProfile = userService.login(loginName, password, false);
		} catch (InstanceNotFoundException e) {
			loginForm.recordError(messages.get("error-authenticationFailed"));
		} catch (IncorrectPasswordException e) {
			loginForm.recordError(messages.get("error-authenticationFailed"));
		}

	}

	/**
	 * @return the product Id.
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            the product Id.
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	/**
	 * The onActivate.
	 * 
	 * @param productId
	 *            the product Id.
	 */
	public void onActivate(Long productId) {
		this.productId = productId;
	}

	/**
	 * @return the object.
	 */
	public Object[] onPassivate() {
		return new Object[] { productId };
	}

	/**
	 * @return the MakeBid
	 */
	public Object onSuccess() {

		userSession = new UserSession();
		userSession.setUserProfileId(userProfile.getUserProfileId());
		userSession.setFirstName(userProfile.getFirstName());

		if (rememberMyPassword) {
			CookiesManager.leaveCookies(cookies, loginName, userProfile.getEncryptedPassword());
		}

		if (productId == null) {
			return Index.class;
		} else {
			makeBid.setProdId(productId);
			return makeBid;
		}
	}

}
