package es.udc.pa.pa007.auctionhouse.model.category;

import es.udc.pojo.modelutil.dao.GenericDao;
import java.util.List;

public interface CategoryDao extends GenericDao<Category, Long>{    
    /**
     * Returns all Category (not category identifier)
     *
     *
     * @return List with all Categories
     */
    public List<Category> getAllCategories();
    
}
