package es.udc.pa.pa007.auctionhouse.model.product;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("ProductDao")
public class ProductDaoHibernate extends
		GenericDaoHibernate<Product, Long> implements ProductDao {
	
	
	@SuppressWarnings("unchecked")
	public List<Product> findByKeyAndCategory(String key, Long categoryId, int startIndex, int count){
		String[] keys = key.split(" ");
		String predicado="";
		int i;
		if ( (key.isEmpty()) && (categoryId == null) ){
			return findActives(startIndex, count);
		}
		if ( !key.isEmpty() ){
			for (i=0 ; i< keys.length; i++){
				predicado = predicado+"prodName LIKE ? AND ";
			}
		}	
		String consulta =
				 "SELECT u FROM Product u WHERE "+ predicado +
				 "u.finishDate > now()";
				
		if ( categoryId != null){
			consulta = consulta +" AND u.category.catId = :categoryId";
		}
		Query query = getSession().createQuery(consulta + " ORDER BY u.createDate");
		if ( !key.isEmpty() ){
			for (i=0 ; i< keys.length; i++){
				query.setParameter(i, "%"+keys[i]+"%");
			}
		}	
		if ( categoryId != null){
			query.setParameter("categoryId", categoryId);
		}
		query.setFirstResult(startIndex).setMaxResults(count);
		return (List<Product>) query.list();
	}			

	@SuppressWarnings("unchecked")
	private List<Product> findActives(int startIndex, int count){
		
		return getSession().createQuery(
				 "SELECT u FROM Product u WHERE u.finishDate > now()" +
				 " ORDER BY u.createDate").
				 setFirstResult(startIndex).
				 setMaxResults(count).list(); 
	}

	/*@SuppressWarnings("unchecked")
	private List<Product> findByCategory(Long categoryId, int startIndex, int count) {
		
		return getSession().createQuery(
				 "SELECT u FROM Product u WHERE u.category.catId = :categoryId AND u.finishDate > now()" +
				 " ORDER BY u.createDate").
				 setParameter("categoryId", categoryId).
				 setFirstResult(startIndex).
				 setMaxResults(count).list(); 
	}
	
	private List<Product> filter(String[] keys, List<Product> products){
		for (String key : keys){
			products.removeIf( p -> !p.getProdName().toLowerCase().contains(key.toLowerCase()) );
		}
		return products;
	}*/
	
	
	@SuppressWarnings("unchecked")
	public List<Product> findByOwner(Long owner, int startIndex, int count){
		
		return getSession().createQuery(
				 "SELECT u FROM Product u WHERE u.owner.userProfileId = :owner" +
				 " ORDER BY u.finishDate DESC").
				 setParameter("owner", owner).
				 setFirstResult(startIndex).
				 setMaxResults(count).list(); 
	}	
	
	public int getNumberOfProducts(Long userId) {

			long numberOfProducts = (Long) getSession().createQuery(
	                "SELECT COUNT(o) FROM Product o WHERE " +
	                "o.owner.userProfileId = :userId").
	                setParameter("userId", userId).
	                uniqueResult();

	        return (int) numberOfProducts;

		}

	@Override
	public int getNumberOfSearchProducts(String keywords, Long catId) {
		String[] keys = keywords.split(" ");
		String predicado="";
		int i;
		if ( (keywords.isEmpty()) && (catId == null) ){
			return getSession().createQuery(
					 "SELECT u FROM Product u WHERE u.finishDate > now()" +
					 " ORDER BY u.createDate").
					 list().size();
		}
		if ( !keywords.isEmpty() ){
			for (i=0 ; i< keys.length; i++){
				predicado = predicado+"prodName LIKE ? AND ";
			}
		}	
		String consulta =
				 "SELECT u FROM Product u WHERE "+ predicado +
				 "u.finishDate > now()";
				
		if ( catId != null){
			consulta = consulta +" AND u.category.catId = :categoryId";
		}
		Query query = getSession().createQuery(consulta + " ORDER BY u.createDate");
		if ( !keywords.isEmpty() ){
			for (i=0 ; i< keys.length; i++){
				query.setParameter(i, "%"+keys[i]+"%");
			}
		}	
		if ( catId != null){
			query.setParameter("categoryId", catId);
		}
		
		return query.list().size();
	}
}