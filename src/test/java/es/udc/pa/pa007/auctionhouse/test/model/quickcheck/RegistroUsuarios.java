package es.udc.pa.pa007.auctionhouse.test.model.quickcheck;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import static org.junit.Assert.assertEquals;

import org.junit.runner.RunWith;

import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;

@RunWith(JUnitQuickcheck.class)
public class RegistroUsuarios {

	// Ejemplo para crear un String aleatorio usando MyCharacterGenerator:
	// @From(MyCharacterGenerator.class) String s

	@Property(trials = 1)
	public void registrationRandomTest(String name, String password, String lastName, String user, String email) {

		System.out.println("Generated data for test: " + user + ", " + name + ", " + lastName + ".");

		UserProfile userProfile;

		userProfile = new UserProfile(user, password, name, lastName, email);

		/* Check data. */
		assertEquals(name, userProfile.getFirstName());
		assertEquals(password, userProfile.getFirstName());
		assertEquals(lastName, userProfile.getLastName());
		assertEquals(email, userProfile.getEmail());
		assertEquals(user, userProfile.getLoginName());

	}

}
