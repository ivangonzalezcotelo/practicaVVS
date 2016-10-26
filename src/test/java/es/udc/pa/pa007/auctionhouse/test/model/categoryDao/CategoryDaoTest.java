package es.udc.pa.pa007.auctionhouse.test.model.categoryDao;

import static es.udc.pa.pa007.auctionhouse.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa007.auctionhouse.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.category.CategoryDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class CategoryDaoTest {

	@Autowired
	private  CategoryDao categoryDao;
	
	@Before
	public void initialize(){
		
		Category cat1 = new Category("Musica");
		categoryDao.save(cat1);
		
		Category cat2 = new Category("Deportes");
		categoryDao.save(cat2);
		
		Category cat3 = new Category("Electrónica");
		categoryDao.save(cat3);
		
	}
	
	
	/* PR-UN-022 */
	@Test
	public void getAllCategoriesTest(){
		
		List<Category> list;
		
		list = categoryDao.getAllCategories();
		assertEquals(3, list.size());
	}
	
}
