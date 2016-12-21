package es.udc.pa.pa007.auctionhouse.model.userservice;

/**
 * IncorrectPasswordException.
 *
 */
@SuppressWarnings("serial")
public class IncorrectPasswordException extends Exception {

    /**
     * The login name.
     */
    private String loginName;

    /**
     * @param loginName the login name.
     */
    public IncorrectPasswordException(String loginName) {
        super("Incorrect password exception => loginName = " + loginName);
        this.loginName = loginName;
    }

    /**
     * @return the login name.
     */ 
    public String getLoginName() {
        return loginName;
    }


}
