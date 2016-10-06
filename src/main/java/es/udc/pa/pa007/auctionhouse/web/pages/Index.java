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

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class Index {
	
	@InjectPage
	private ProductsResults productsResults;
	
	@Property
	private String productName;
	
	@Component
    private Form searchForm;
	
	@Inject
	private ProductService productService;

	@Inject
	private Messages messages;
		
	@Property
	private SelectModel categorysModel;
	
	@Property
	private Category category;
	
	@Property
	private Long catOption;
	
    @Inject
    private Locale locale;
	
	@Inject
	private SelectModelFactory selectModelFactory;
	
	List<String> onProvideCompletionsFromProductName(String partial) throws InstanceNotFoundException {
        List<String> matches = new ArrayList<String>();
        partial = partial.toUpperCase();
        for (Product productName : productService.findActiveAuctions(partial, null, 0, 5)) {
            matches.add(productName.getProdName());
        }

        return matches;
    }
	
	public CategoryEncoder getCategoryEncoder() {
		List<Category> categorys = productService.getAllCategories();
		categorysModel = selectModelFactory.create(categorys, "catName");
		return new CategoryEncoder(categorysModel);
	}
	
	void onValidateFromSearchForm() {
        if (productName == null){
        	productName = new String("");
        }
        
        if (category == null){
			catOption = null;
		} else{
			catOption = category.getCatId();
		}
   }
	
	Object onSuccess(){
		return productsResults.set(productName, catOption);
	}
	
}
