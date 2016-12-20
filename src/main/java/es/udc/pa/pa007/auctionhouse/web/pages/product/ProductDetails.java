package es.udc.pa.pa007.auctionhouse.web.pages.product;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicy;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicyType;
import es.udc.pa.pa007.auctionhouse.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class ProductDetails {

	@SessionState(create = false)
	private UserSession userSession;

	@Property
	private Product product;

	@Property
	private Long productId;

	@Property
	private String remainingTime;

	@Property
	private String owner;

	@Property
	private String category;

	@Property
	private int login;

	@Property
	private String currentWinner;

	@Inject
	private Messages messages;

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	@Inject
	private ProductService productService;

	@Inject
	private Locale locale;

	public Format getFormat() {
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		return formatter;
	}

	Object[] onPassivate() {
		return new Object[] { productId };
	}

	void onActivate(Long productId) {
		this.productId = productId;
		try {
			product = productService.findByProductId(productId);
		} catch (InstanceNotFoundException e) {
			return;
		}
		// Comprobación de si la puja ya ha finalizado o si el producto es tuyo
		login = 1;

		/*
		 * if((userSession.getUserProfileId() ==
		 * product.getOwner().getUserProfileId()) || (product.getTimeRemaining()
		 * <= 0)){ login = 0; }
		 */
		if (userSession == null) {
			login = 0;
		}

		if (product.getWinnerBid() != null) {
			currentWinner = product.getWinnerBid().getUserId().getLoginName();
			product.setActualPrice(product.getActualPrice());
		} else {
			currentWinner = messages.get("noBids");
			product.setActualPrice(product.getLaunchPrice());
		}
		if (product.getTimeRemaining() < 0) {
			this.remainingTime = messages.get("finalized");
		} else {
			this.remainingTime = String.valueOf(product.getTimeRemaining());
		}
		owner = product.getOwner().getLoginName();
		category = product.getCategory().getCatName();
	}
}
