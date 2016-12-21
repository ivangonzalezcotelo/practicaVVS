package es.udc.pa.pa007.auctionhouse.model.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfileDao;
import es.udc.pa.pa007.auctionhouse.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * Implementation.
 *
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    /**
     * The UserProfileDao.
     */
    @Autowired
    private UserProfileDao userProfileDao;

    /**
     * {@inheritDoc}
     */
    public UserProfile registerUser(String loginName, String clearPassword,
            UserProfileDetails userProfileDetails)
            throws DuplicateInstanceException {

        try {
            userProfileDao.findByLoginName(loginName);
            throw new DuplicateInstanceException(loginName,
                    UserProfile.class.getName());
        } catch (InstanceNotFoundException e) {
            String encryptedPassword = PasswordEncrypter.crypt(clearPassword);

            UserProfile userProfile = new UserProfile(loginName,
                    encryptedPassword, userProfileDetails.getFirstName(),
                    userProfileDetails.getLastName(), userProfileDetails
                        .getEmail());

            userProfileDao.save(userProfile);
            return userProfile;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public UserProfile login(String loginName, String password,
            boolean passwordIsEncrypted) throws InstanceNotFoundException,
            IncorrectPasswordException {

        UserProfile userProfile = userProfileDao.findByLoginName(loginName);
        String storedPassword = userProfile.getEncryptedPassword();

        if (passwordIsEncrypted) {
            if (!password.equals(storedPassword)) {
                throw new IncorrectPasswordException(loginName);
            }
        } else {
            if (!PasswordEncrypter.isClearPasswordCorrect(password,
                    storedPassword)) {
                throw new IncorrectPasswordException(loginName);
            }
        }
        return userProfile;

    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public UserProfile findUserProfile(Long userProfileId)
            throws InstanceNotFoundException {

        return userProfileDao.find(userProfileId);
    }

    /**
     * {@inheritDoc}
     */
    public void updateUserProfileDetails(Long userProfileId,
            UserProfileDetails userProfileDetails)
            throws InstanceNotFoundException {

        UserProfile userProfile = userProfileDao.find(userProfileId);
        userProfile.setFirstName(userProfileDetails.getFirstName());
        userProfile.setLastName(userProfileDetails.getLastName());
        userProfile.setEmail(userProfileDetails.getEmail());

    }

    /**
     * {@inheritDoc}
     */
    public void changePassword(Long userProfileId, String oldClearPassword,
            String newClearPassword) throws IncorrectPasswordException,
            InstanceNotFoundException {

        UserProfile userProfile;
        userProfile = userProfileDao.find(userProfileId);

        String storedPassword = userProfile.getEncryptedPassword();

        if (!PasswordEncrypter.isClearPasswordCorrect(oldClearPassword,
                storedPassword)) {
            throw new IncorrectPasswordException(userProfile.getLoginName());
        }

        userProfile.setEncryptedPassword(PasswordEncrypter
                .crypt(newClearPassword));

    }
    
}
