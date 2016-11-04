package es.udc.pa.pa007.auctionhouse.test.model.userservice;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfileDao;
import es.udc.pa.pa007.auctionhouse.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa007.auctionhouse.model.userservice.UserProfileDetails;

import es.udc.pa.pa007.auctionhouse.model.userservice.UserServiceImpl;
import es.udc.pa.pa007.auctionhouse.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {

	
	@InjectMocks
	private UserServiceImpl userService = new UserServiceImpl();
	
	@Mock
	private UserProfileDao userProfileDao;
	
	
	// Test de registro de usuario correcto
	/* PR-UN-005 */
	@Test
	public void testRegisterUser() throws DuplicateInstanceException, InstanceNotFoundException{
		
		UserProfileDetails userProfileDetails = new UserProfileDetails("Carl","Petersen","carl@gmail.com");
		String login = "user";
		String pass = "PASS";
		
		when(userProfileDao.findByLoginName(login)).thenThrow( new InstanceNotFoundException("user5", UserProfile.class.getName()));
		UserProfile user = userService.registerUser(login, pass, userProfileDetails);
		
		assertTrue(PasswordEncrypter.isClearPasswordCorrect(pass,user.getEncryptedPassword()));
		assertEquals(user.getFirstName(), userProfileDetails.getFirstName());
		assertEquals(user.getLastName(), userProfileDetails.getLastName());
		assertEquals(user.getEmail(), userProfileDetails.getEmail());
		assertEquals(user.getLoginName(),login);
		
		
	}
	
	// Test de registro de usuario ya existente
	/* PR-UN-006 */
	@Test(expected=DuplicateInstanceException.class)
	public void testRegisterUserExistent() throws DuplicateInstanceException, InstanceNotFoundException{
		
		UserProfileDetails userProfileDetails = new UserProfileDetails("Carl","Petersen","carl@gmail.com");
		String login = "user";
		String pass = "PASS";
		
		UserProfile user= new UserProfile(login, pass, userProfileDetails.getFirstName(),
                    userProfileDetails.getLastName(), userProfileDetails.getEmail());
		
		when(userProfileDao.findByLoginName(login)).thenReturn(user);
		userService.registerUser(login, pass, userProfileDetails);
	}

	// Test de login correcto con la pass sin encriptar
	/* PR-UN-007 */
	@Test
	public void testLogin() throws InstanceNotFoundException, IncorrectPasswordException, DuplicateInstanceException{
				
		String login = "user";
		String pass = "PASS";
		
		UserProfile user1= new UserProfile(login, PasswordEncrypter.crypt(pass), "Johann", "Petersen", "johann@gmail.com");
		when(userProfileDao.findByLoginName(login)).thenReturn(user1);
		UserProfile user2 = userService.login(login,pass,Boolean.FALSE);
		
		assertEquals(user1.getLoginName(),user2.getLoginName());
		assertEquals(user1.getFirstName(),user2.getFirstName());
		assertEquals(user1.getLastName(),user2.getLastName());
		assertEquals(user1.getEncryptedPassword(),user2.getEncryptedPassword());
		assertEquals(user1.getEmail(),user2.getEmail());
		
	}	
	
	// Test de login correcto con la pass encriptada
	/* PR-UN-008 */
	@Test
	public void testLoginEncrypted() throws InstanceNotFoundException, IncorrectPasswordException, DuplicateInstanceException{
				
		String login = "user";
		String pass = "PASS";
		
		UserProfile user1= new UserProfile(login, pass, "Johann", "Petersen", "johann@gmail.com");
		when(userProfileDao.findByLoginName(login)).thenReturn(user1);
		UserProfile user2 = userService.login(login,pass,Boolean.TRUE);
		
		assertEquals(user1.getLoginName(),user2.getLoginName());
		assertEquals(user1.getFirstName(),user2.getFirstName());
		assertEquals(user1.getLastName(),user2.getLastName());
		assertEquals(user1.getEncryptedPassword(),user2.getEncryptedPassword());
		assertEquals(user1.getEmail(),user2.getEmail());
		
	}
	
	// Test de login incorrecto con la pass sin encriptar
	/* PR-UN-009 */
	@Test(expected=IncorrectPasswordException.class)
	public void testLoginBadPass() throws InstanceNotFoundException, IncorrectPasswordException, DuplicateInstanceException{
		
		String login = "user";
		String pass = "PASS";
		String badPass = "BADPASS";
		
		UserProfile user1= new UserProfile(login, PasswordEncrypter.crypt(pass), "Johann", "Petersen", "johann@gmail.com");
		when(userProfileDao.findByLoginName(login)).thenReturn(user1);
		userService.login(login,badPass,Boolean.FALSE);
		
	}	
	
	// Test de login incorrecto con la pass encriptada
	/* PR-UN-010 */
	@Test(expected=IncorrectPasswordException.class)
	public void testLoginEncryptedBadPass() throws InstanceNotFoundException, IncorrectPasswordException, DuplicateInstanceException{
				
		String login = "user";
		String pass = "PASS";
		String badPass = "BADPASS";
		
		UserProfile user1= new UserProfile(login, pass, "Johann", "Petersen", "johann@gmail.com");
		when(userProfileDao.findByLoginName(login)).thenReturn(user1);
		userService.login(login,badPass,Boolean.TRUE);
		
	}
	
	/* Test de busqueda de usuario por id de usuario existente */
	/* PR-UN-011 */
	@Test
	public void testFindUser() throws InstanceNotFoundException{
				
		Long id = Long.valueOf(1);

		UserProfile user1= new UserProfile("user", "PASS", "Johann", "Petersen", "johann@gmail.com");
		when(userProfileDao.find(id)).thenReturn(user1);
		UserProfile user2 = userService.findUserProfile(id);
		
		assertEquals(user1.getLoginName(),user2.getLoginName());
		assertEquals(user1.getFirstName(),user2.getFirstName());
		assertEquals(user1.getLastName(),user2.getLastName());
		assertEquals(user1.getEncryptedPassword(),user2.getEncryptedPassword());
		assertEquals(user1.getEmail(),user2.getEmail());
	}
	
	/* Test de busqueda de usuario por id de usuario inexistente */
	/* PR-UN-012 */
	@Test(expected=InstanceNotFoundException.class)
	public void testFindUserNonExistent() throws InstanceNotFoundException{
				
		Long id = Long.valueOf(1);
		
		when(userProfileDao.find(id)).thenThrow(new InstanceNotFoundException("user5", UserProfile.class.getName()));
		userService.findUserProfile(id);
	}
	
	
	/* Actualización de datos de usuario  */
	/* PR-UN-013 */
	@Test
	public void testUpdateUserExistent() throws InstanceNotFoundException{
				
		Long id = Long.valueOf(1);
		String fName = "Luck";
		String lName = "Cage";
		String email = "newemail@whatever.com";
		
		UserProfileDetails details = new UserProfileDetails (fName, lName, email);
		UserProfile user = new UserProfile("user", "PASS", details.getFirstName(), details.getLastName(),
				details.getEmail());
		
		when(userProfileDao.find(id)).thenReturn(user);
		userService.updateUserProfileDetails(id,details);

	}
	
	/* Actualización de datos de usuario no existente */
	/* PR-UN-014 */
	@Test(expected=InstanceNotFoundException.class)
	public void testUpdateUserNonExistent() throws InstanceNotFoundException{
				
		Long id = Long.valueOf(1);
		String fName = "Luck";
		String lName = "Cage";
		String email = "newemail@whatever.com";
		UserProfileDetails details = new UserProfileDetails (fName, lName, email);
		
		when(userProfileDao.find(id)).thenThrow(new InstanceNotFoundException("user5", UserProfile.class.getName()));
		userService.updateUserProfileDetails(id,details);

	}
	
	
	/* Actualización de contraseña */
	/* PR-UN-015 */
	@Test
	public void testChangePass() throws InstanceNotFoundException, IncorrectPasswordException{
				
		Long id = Long.valueOf(1);
		String oldPass = "PASS";
		String newPass = "NEWPASS";
		
		UserProfile user= new UserProfile("user", PasswordEncrypter.crypt(oldPass), "Johann", "Petersen", "johann@gmail.com");
		
		when(userProfileDao.find(id)).thenReturn(user);
		userService.changePassword(id,oldPass,newPass);

	}
	
	/* Actualización de contraseña de usuario no existente */
	/* PR-UN-016 */
	@Test(expected=InstanceNotFoundException.class)
	public void testChangePassUserNonExistent() throws InstanceNotFoundException, IncorrectPasswordException{
				
		Long id = Long.valueOf(1);
		String oldPass = "PASS";
		String newPass = "NEWPASS";
		
		when(userProfileDao.find(id)).thenThrow(new InstanceNotFoundException("user5", UserProfile.class.getName()));
		userService.changePassword(id,oldPass,newPass);

	}
	

	
	/* Actualización de contraseña con contraseña antigua mal */
	/* PR-UN-017 */
	@Test(expected=IncorrectPasswordException.class)
	public void testChangePassBadPass() throws InstanceNotFoundException, IncorrectPasswordException{
				
		Long id = Long.valueOf(1);
		String oldPass = "PASS";
		String randomPass = "RANDOM";
		String newPass = "NEWPASS";
		
		UserProfile user= new UserProfile("user", PasswordEncrypter.crypt(oldPass), "Johann", "Petersen", "johann@gmail.com");
		
		when(userProfileDao.find(id)).thenReturn(user);
		userService.changePassword(id,randomPass,newPass);

	}
	
}	

