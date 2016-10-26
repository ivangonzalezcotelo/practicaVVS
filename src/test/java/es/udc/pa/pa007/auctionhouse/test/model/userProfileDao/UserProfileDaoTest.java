package es.udc.pa.pa007.auctionhouse.test.model.userProfileDao;

import static es.udc.pa.pa007.auctionhouse.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa007.auctionhouse.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class UserProfileDaoTest {

	@Autowired
	private  UserProfileDao userProfileDao;
	
	/* Test de busqueda de usuario existente por login */
	
	@Test	
	public void testFindByLogin() throws InstanceNotFoundException{
    	
		UserProfile user1 = new UserProfile("user", "PASS", "John", "Day", "john@gmail.com" );
		String login = user1.getLoginName();
    	userProfileDao.save(user1);
		UserProfile user2 = userProfileDao.findByLoginName(login);
    	assertEquals(user1.getLoginName(),user2.getLoginName());
    	assertEquals(user1.getFirstName() ,user2.getFirstName());
    	assertEquals(user1.getLastName(),user2.getLastName());
    	assertEquals(user1.getEncryptedPassword() ,user2.getEncryptedPassword());
    	assertEquals(user1.getEmail() ,user2.getEmail());
   			
	}
	
	/* Test de busqueda de usuario no existente por login */
	
	@Test(expected=InstanceNotFoundException.class)	
	public void testFindByLoginUserNonExistent() throws InstanceNotFoundException{
    	
		UserProfile user1 = new UserProfile("user", "PASS", "John", "Day", "john@gmail.com" );
		String login = "NoRealUser";
    	userProfileDao.save(user1);
		UserProfile user2 = userProfileDao.findByLoginName(login);
    	assertEquals(user1.getLoginName(),user2.getLoginName());
    	assertEquals(user1.getFirstName() ,user2.getFirstName());
    	assertEquals(user1.getLastName(),user2.getLastName());
    	assertEquals(user1.getEncryptedPassword() ,user2.getEncryptedPassword());
    	assertEquals(user1.getEmail() ,user2.getEmail());
   			
	}
	
}