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

/**
 * Insert.
 *
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class Insert {

	/**
	 * The product name.
	 */
	@Property
	private String productName;

	/**
	 * The description.
	 */
	@Property
	private String description;

	/**
	 * The launch price.
	 */
	@Property
	private String launchPrice;

	/**
	 * The send info.
	 */
	@Property
	private String sendInfo;

	/**
	 * The launch price as BigDecimal.
	 */
	private BigDecimal launchPriceAsBigDecimal;

	/**
	 * The remaining time.
	 */
	@Property
	private int timeRemaining;

	/**
	 * The Category.
	 */
	@Property
	private Category category;

	/**
	 * The UserSession.
	 */
	@SessionState(create = false)
	private UserSession userSession;

	/**
	 * The launch price TextField.
	 */
	@Component(id = "launchPrice")
	private TextField launchPriceTextField;

	/**
	 * The ProductService.
	 */
	@Inject
	private ProductService productService;

	/**
	 * The insert Form.
	 */
	@Component
	private Form insertForm;

	/**
	 * The product name TextField.
	 */
	@Component(id = "productName")
	private TextField productNameField;

	/**
	 * The Messages.
	 */
	@Inject
	private Messages messages;

	/**
	 * The category model.
	 */
	@Property
	private SelectModel catModel;

	/**
	 * The SelectModelFactory.
	 */
	@Inject
	private SelectModelFactory selectModelFactory;

	/**
	 * The Locale.
	 */
	@Inject
	private Locale locale;

	/**
	 * @return the CategoryEncoder.
	 */
	public CategoryEncoder getCategoryEncoder() {
		List<Category> categories = productService.getAllCategories();
		catModel = selectModelFactory.create(categories, "catName");
		return new CategoryEncoder(catModel);
	}

	/**
	 * The onValidateFromInsertForm.
	 */
	public void onValidateFromInsertForm() {
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

	/**
	 * @return the object SuccesfulInsert.
	 */
	public Object onSuccess() {
		return SuccessfulInsert.class;
	}

}