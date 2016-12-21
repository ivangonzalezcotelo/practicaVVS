package es.udc.pa.pa007.auctionhouse.model.userprofile;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * UserProfileDao.
 *
 */
public interface UserProfileDao extends GenericDao<UserProfile, Long> {

	/**
	 * Returns an UserProfile by login name (not user identifier).
	 *
	 * @param loginName
	 *            the user identifier
	 * @return the UserProfile
	 * @throws InstanceNotFoundException
	 *             if not exist a UserProfile with that login name.
	 */
	UserProfile findByLoginName(String loginName) throws InstanceNotFoundException;
}