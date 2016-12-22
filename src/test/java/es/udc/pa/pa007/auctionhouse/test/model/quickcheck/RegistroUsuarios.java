package es.udc.pa.pa007.auctionhouse.test.model.quickcheck;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import static org.junit.Assert.assertEquals;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserProfileDetails;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;

@RunWith(JUnitQuickcheck.class)
public class RegistroUsuarios {
	
	@Autowired
	UserService userService;
	
	@Property(trials = 5)
	public void registrationRandomTest(String name, String lastName, String email, String user) {

		System.out.println("Generated data for test: " + user + ", " + name + ", " + lastName + ", " + email + ".");
		
		/* Register user and find profile. */
		UserProfile userProfile;
		try {
			userProfile = userService.registerUser(user, "password", new UserProfileDetails(name, lastName, email));

			/* Check data. */
			assertEquals(name, userProfile.getFirstName());
			assertEquals(lastName, userProfile.getLastName());
			assertEquals(email, userProfile.getEmail());
			assertEquals(user, userProfile.getLoginName());

		} catch (DuplicateInstanceException e) {
			e.printStackTrace();
		}

	}

}
