package es.udc.pa.pa007.auctionhouse.web.services;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AuthenticationPolicy.
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthenticationPolicy {
	/**
	 * @return the AuthenticationPolicyType.
	 */
	AuthenticationPolicyType value() default AuthenticationPolicyType.ALL_USERS;
}
