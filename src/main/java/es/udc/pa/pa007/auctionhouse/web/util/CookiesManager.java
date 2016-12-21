package es.udc.pa.pa007.auctionhouse.web.util;

import org.apache.tapestry5.services.Cookies;

/**
 * CookiesManager.
 *
 */
public class CookiesManager {

	/**
	 * The login name cookie.
	 */
	private static final String LOGIN_NAME_COOKIE = "loginName";
	/**
	 * The encrypted password cookie.
	 */
	private static final String ENCRYPTED_PASSWORD_COOKIE = "encryptedPassword";
    /**
     * The time to remember password.
     */
    private static final int REMEMBER_MY_PASSWORD_AGE =
        30 * 24 * 3600; // 30 days in seconds

	/**
	 * @param cookies the Cookies.
	 * @param loginName the login name.
	 * @param encryptedPassword the encryptedPassword.
	 */
	public static void leaveCookies(Cookies cookies, String loginName,
			String encryptedPassword) {
		
		cookies.getBuilder(LOGIN_NAME_COOKIE, loginName).
			setMaxAge(REMEMBER_MY_PASSWORD_AGE).write();
		cookies.getBuilder(ENCRYPTED_PASSWORD_COOKIE, encryptedPassword).
			setMaxAge(REMEMBER_MY_PASSWORD_AGE).write();

	}

	/**
	 * @param cookies the Cookies.
	 */
	public static void removeCookies(Cookies cookies) {
		cookies.removeCookieValue(LOGIN_NAME_COOKIE);
		cookies.removeCookieValue(ENCRYPTED_PASSWORD_COOKIE);
	}

	/**
	 * @param cookies the Cookies.
	 * @return the cookies value.
	 */
	public static String getLoginName(Cookies cookies) {
		return cookies.readCookieValue(LOGIN_NAME_COOKIE);
	}

	/**
	 * @param cookies the Cookies.
	 * @return the cookie value.
	 */
	public static String getEncryptedPassword(Cookies cookies) {
		return cookies.readCookieValue(ENCRYPTED_PASSWORD_COOKIE);
	}

}
