package es.udc.pa.pa007.auctionhouse.web.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pa.pa007.auctionhouse.web.pages.product.ProductsResults;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicy;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicyType;
import es.udc.pa.pa007.auctionhouse.web.util.CategoryEncoder;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * Index.
 *
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class Index {
	
	/**
	 * The index.
	 */
	private final int index = 5;

	/**
	 * The ProductsResults.
	 */
	@InjectPage
	private ProductsResults productsResults;

	/**
	 * The product name.
	 */
	@Property
	private String productName;

	/**
	 * The search Form.
	 */
	@Component
	private Form searchForm;

	/**
	 * The ProductService.
	 */
	@Inject
	private ProductService productService;

	/**
	 * The Messages.
	 */
	@Inject
	private Messages messages;

	/**
	 * The categorysModel.
	 */
	@Property
	private SelectModel categorysModel;

	/**
	 * The Category.
	 */
	@Property
	private Category category;

	/**
	 * The category option.
	 */
	@Property
	private Long catOption;

	/**
	 * The Locale.
	 */
	@Inject
	private Locale locale;

	/**
	 * The SelectModelFactory.
	 */
	@Inject
	private SelectModelFactory selectModelFactory;

	/**
	 * @param partial the partial.
	 * @return the products which name matches with partial.
	 * @throws InstanceNotFoundException if not exist the category(never expected cause is always null).
	 */
	public List<String> onProvideCompletionsFromProductName(String partial) throws InstanceNotFoundException {
		List<String> matches = new ArrayList<String>();
		partial = partial.toUpperCase();
		for (Product productName : productService.findActiveAuctions(partial, null, 0, index)) {
			matches.add(productName.getProdName());
		}

		return matches;
	}

	/**
	 * @return the CategoryEncoder.
	 */
	public CategoryEncoder getCategoryEncoder() {
		List<Category> categorys = productService.getAllCategories();
		categorysModel = selectModelFactory.create(categorys, "catName");
		return new CategoryEncoder(categorysModel);
	}

	/**
	 * Search initiation.
	 */
	public void onValidateFromSearchForm() {
		if (productName == null) {
			productName = new String("");
		}

		if (category == null) {
			catOption = null;
		} else {
			catOption = category.getCatId();
		}
	}

	/**
	 * @return the Object.
	 */
	public Object onSuccess() {
		return productsResults.set(productName, catOption);
	}

}
