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

@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Login {

	@Property
	private String loginName;

	@Property
	private String password;

	@Property
	private boolean rememberMyPassword;

	@SessionState(create = false)
	private UserSession userSession;

	@Inject
	private Cookies cookies;

	@Component
	private Form loginForm;

	@Inject
	private Messages messages;

	@Inject
	private UserService userService;

	private UserProfile userProfile = null;

	private Long productId;

	@InjectPage
	private MakeBid makeBid;

	void onValidateFromLoginForm() {

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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	void onActivate(Long productId) {
		this.productId = productId;
	}

	Object[] onPassivate() {
		return new Object[] { productId };
	}

	Object onSuccess() {

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
