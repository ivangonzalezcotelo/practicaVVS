package es.udc.pa.pa007.auctionhouse.web.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.pa.pa007.auctionhouse.web.pages.Index;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicy;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicyType;
import es.udc.pa.pa007.auctionhouse.web.util.CookiesManager;
import es.udc.pa.pa007.auctionhouse.web.util.UserSession;

/**
 * Layout.
 *
 */
@Import(library = { "tapestry5/bootstrap/js/collapse.js",
		"tapestry5/bootstrap/js/dropdown.js" }, stylesheet = "tapestry5/bootstrap/css/bootstrap-theme.css")
public class Layout {

	/**
	 * The title.
	 */
	@Property
	@Parameter(required = true, defaultPrefix = "message")
	private String title;

	/**
	 * The boolean showTitleInBody.
	 */
	@Parameter(defaultPrefix = "literal")
	private Boolean showTitleInBody;

	/**
	 * The UserSession.
	 */
	@Property
	@SessionState(create = false)
	private UserSession userSession;

	/**
	 * The cookies.
	 */
	@Inject
	private Cookies cookies;

	/**
	 * @return the showTitleInBody.
	 */
	public boolean getShowTitleInBody() {

		if (showTitleInBody == null) {
			return true;
		} else {
			return showTitleInBody;
		}

	}

	/**
	 * @return the Index.
	 */
	@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
	Object onActionFromLogout() {
		userSession = null;
		CookiesManager.removeCookies(cookies);
		return Index.class;
	}

}
