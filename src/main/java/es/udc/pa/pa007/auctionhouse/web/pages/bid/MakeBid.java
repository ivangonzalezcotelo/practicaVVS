package es.udc.pa.pa007.auctionhouse.web.pages.bid;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa007.auctionhouse.model.bidservice.BidService;
import es.udc.pa.pa007.auctionhouse.model.bidservice.InvalidBidException;
import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pa.pa007.auctionhouse.web.pages.user.Login;
import es.udc.pa.pa007.auctionhouse.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class MakeBid {

	@SessionState(create = false)
	private UserSession userSession;

	@Property
	private Product product;

	// Locale
	private BigDecimal maxValueAsBigDecimal;

	@Property
	private Long productId;

	@Property
	private String remainingTime;

	@Property
	private String owner;

	@Property
	private String category;

	@Property
	private String maxPrice;

	@Property
	private BigDecimal actualPrice;

	@Component
	private Form makeBidForm;

	@Component(id = "maxPrice")
	private TextField maxValueTextField;

	@Inject
	private Messages messages;

	@Inject
	private ProductService productService;

	@Inject
	private BidService bidService;

	@InjectPage
	private Login login;

	@Inject
	private Locale locale;

	public Format getFormat() {
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		return formatter;
	}

	Object onActivate(Long productId) {

		if (userSession == null) {
			login.setProductId(productId);
			return login;
		}

		this.productId = productId;
		try {
			product = productService.findByProductId(productId);
		} catch (InstanceNotFoundException e) {
			return null;
		}

		if (product.getTimeRemaining() < 0) {
			this.remainingTime = messages.get("finished");
		} else {
			this.remainingTime = String.valueOf(product.getTimeRemaining());
		}

		owner = product.getOwner().getLoginName();
		category = product.getCategory().getCatName();

		if (product.getWinnerBid() != null) {
			actualPrice = product.getActualPrice();
		} else {
			actualPrice = product.getLaunchPrice();
		}
		return null;
	}

	public void setProdId(Long productId) {
		this.productId = productId;
	}

	Object[] onPassivate() {
		return new Object[] { productId };
	}

	void onValidateFromMakeBidForm() {
		if (!makeBidForm.isValid()) {
			return;
		}

		NumberFormat numberFormatter = NumberFormat.getInstance(locale);
		ParsePosition position = new ParsePosition(0);
		Number number = numberFormatter.parse(maxPrice, position);

		if (position.getIndex() != maxPrice.length()) {
			makeBidForm.recordError(maxValueTextField, messages.format("error-incorrectNumberFormat", maxPrice));
		} else {
			maxValueAsBigDecimal = new BigDecimal(number.doubleValue());
		}

		try {
			bidService.makeBid(userSession.getUserProfileId(), productId, maxValueAsBigDecimal);
		} catch (InvalidBidException e) {
			makeBidForm.recordError(maxValueTextField, messages.format(e.getReason()));
		} catch (InstanceNotFoundException e) {
			makeBidForm.recordError(maxValueTextField, messages.format("error-productNotFound", productId));
		}
	}

	Object onSuccess() {
		return SuccessfulBid.class;
	}

}