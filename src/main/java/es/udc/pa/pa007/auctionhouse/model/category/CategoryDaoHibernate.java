package es.udc.pa.pa007.auctionhouse.model.category;

import org.springframework.stereotype.Repository;
import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("CategoryDao")
public class CategoryDaoHibernate extends
		GenericDaoHibernate<Category, Long> implements CategoryDao {

	@SuppressWarnings("unchecked")
	public List<Category> getAllCategories() {
		return getSession().createQuery("SELECT u FROM Category u").list();
	}

}
