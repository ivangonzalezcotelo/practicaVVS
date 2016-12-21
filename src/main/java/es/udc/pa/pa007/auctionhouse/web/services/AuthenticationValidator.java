package es.udc.pa.pa007.auctionhouse.web.services;

import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;

import es.udc.pa.pa007.auctionhouse.web.util.UserSession;

/**
 * AuthenticationValidator.
 *
 */
public class AuthenticationValidator {

	/**
	 * The login page.
	 */
	private static final String LOGIN_PAGE = "user/Login";

	/**
	 * The init page.
	 */
	private static final String INIT_PAGE = "Index";

	/**
	 * The page authentication type.
	 */
	public static final String PAGE_AUTHENTICATION_TYPE = "page-authentication-type";
	/**
	 * The event handler authentication type.
	 */
	public static final String EVENT_HANDLER_AUTHENTICATION_TYPE = "event-handler-authentication-type";

	/**
	 * @param pageName the page name.
	 * @param applicationStateManager the ApplicationStateManager.
	 * @param componentSource the ComponentSource.
	 * @param locator the locator.
	 * @return the redirect page.
	 */
	public static String checkForPage(String pageName,
			ApplicationStateManager applicationStateManager,
			ComponentSource componentSource, MetaDataLocator locator) {

		String redirectPage = null;
		Component page = componentSource.getPage(pageName);
		try {
			String policyAsString = locator.findMeta(PAGE_AUTHENTICATION_TYPE,
					page.getComponentResources(), String.class);

			AuthenticationPolicyType policy = AuthenticationPolicyType
					.valueOf(policyAsString);
			redirectPage = check(policy, applicationStateManager);
		} catch (RuntimeException e) {
			System.out.println("Page: '" + pageName + "': " + e.getMessage());
		}
		return redirectPage;

	}

	/**
	 * @param pageName the page name.
	 * @param componentId the component Id.
	 * @param eventId the event Id.
	 * @param eventType the event type.
	 * @param applicationStateManager the ApplicationStateManager.
	 * @param componentSource the ComponentSource.
	 * @param locator the locator.
	 * @return the redirect page.
	 */
	public static String checkForComponentEvent(String pageName,
			String componentId, String eventId, String eventType,
			ApplicationStateManager applicationStateManager,
			ComponentSource componentSource, MetaDataLocator locator) {

		String redirectPage = null;
		String authenticationPolicyMeta = EVENT_HANDLER_AUTHENTICATION_TYPE
				+ "-" + eventId + "-" + eventType;
		authenticationPolicyMeta = authenticationPolicyMeta.toLowerCase();

		Component component = null;
		if (componentId == null) {
			component = componentSource.getPage(pageName);
		} else {
			component = componentSource.getComponent(pageName + ":"
					+ componentId);
		}
		try {
			String policyAsString = locator.findMeta(authenticationPolicyMeta,
					component.getComponentResources(), String.class);
			AuthenticationPolicyType policy = AuthenticationPolicyType
					.valueOf(policyAsString);
			redirectPage = AuthenticationValidator.check(policy,
					applicationStateManager);
		} catch (RuntimeException e) {
			System.out.println("Component: '" + pageName + ":" + componentId
					+ "': " + e.getMessage());
		}
		return redirectPage;

	}

	/**
	 * @param policy the policy.
	 * @param applicationStateManager the ApplicationStateManager.
	 * @return the redirect page.
	 */
	public static String check(AuthenticationPolicy policy,
			ApplicationStateManager applicationStateManager) {

		if (policy != null) {
			return check(policy.value(), applicationStateManager);
		} else {
			return null;
		}

	}

	/**
	 * @param policyType the policy type.
	 * @param applicationStateManager the ApplicationStateManager.
	 * @return the redirect page.
	 */
	public static String check(AuthenticationPolicyType policyType,
			ApplicationStateManager applicationStateManager) {
		String redirectPage = null;

		boolean userAuthenticated = applicationStateManager
				.exists(UserSession.class);

		switch (policyType) {

		case AUTHENTICATED_USERS:

			if (!userAuthenticated) {
				redirectPage = LOGIN_PAGE;
			}
			break;

		case NON_AUTHENTICATED_USERS:

			if (userAuthenticated) {
				redirectPage = INIT_PAGE;
			}
			break;

		default:
			break;

		}

		return redirectPage;
	}

}
