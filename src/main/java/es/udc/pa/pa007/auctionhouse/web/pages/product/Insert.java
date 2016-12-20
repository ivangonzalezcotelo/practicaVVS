package es.udc.pa.pa007.auctionhouse.web.pages.product;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicy;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicyType;
import es.udc.pa.pa007.auctionhouse.web.util.CategoryEncoder;
import es.udc.pa.pa007.auctionhouse.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class Insert {

	@Property
	private String productName;

	@Property
	private String description;

	@Property
	private String launchPrice;

	@Property
	private String sendInfo;

	private BigDecimal launchPriceAsBigDecimal;

	@Property
	private int timeRemaining;

	@Property
	private Category category;

	@SessionState(create = false)
	private UserSession userSession;

	@Component(id = "launchPrice")
	private TextField launchPriceTextField;

	@Inject
	private ProductService productService;

	@Component
	private Form insertForm;

	@Component(id = "productName")
	private TextField productNameField;

	@Inject
	private Messages messages;

	@Property
	private SelectModel catModel;

	@Inject
	private SelectModelFactory selectModelFactory;

	@Inject
	private Locale locale;

	public CategoryEncoder getCategoryEncoder() {
		List<Category> categories = productService.getAllCategories();
		catModel = selectModelFactory.create(categories, "catName");
		return new CategoryEncoder(catModel);
	}

	void onValidateFromInsertForm() {
		if (!insertForm.isValid()) {
			return;
		}

		NumberFormat numberFormatter = NumberFormat.getInstance(locale);
		ParsePosition position = new ParsePosition(0);
		Number number = numberFormatter.parse(launchPrice, position);

		if (position.getIndex() != launchPrice.length()) {
			insertForm.recordError(launchPriceTextField, messages.format("error-incorrectNumberFormat", launchPrice));
		} else {
			launchPriceAsBigDecimal = new BigDecimal(number.doubleValue());
		}

		try {
			productService.insertProduct(productName, description, launchPriceAsBigDecimal, sendInfo, timeRemaining,
					userSession.getUserProfileId(), category.getCatId());
		} catch (InstanceNotFoundException e) {
			return;
		}
	}

	Object onSuccess() {
		return SuccessfulInsert.class;
	}

}