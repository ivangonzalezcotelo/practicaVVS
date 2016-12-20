package es.udc.pa.pa007.auctionhouse.web.pages.product;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pa.pa007.auctionhouse.web.util.ProductGridDataSource;
import es.udc.pa.pa007.auctionhouse.web.util.UserSession;

public class OwnProducts {

	private final static int ROWS_PER_PAGE = 4;

	private Long userId;
	private ProductGridDataSource productGridDataSource;
	private Product product;

	@Property
	private String winnerBid;

	@Property
	private BigDecimal actualPrice;

	@Property
	private String timeRemaining;

	@Inject
	private Messages messages;

	@SessionState(create = false)
	private UserSession userSession;

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

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public ProductGridDataSource getProductGridDataSource() {
		return productGridDataSource;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
		if (product.getWinnerBid() != null) {
			this.winnerBid = product.getWinnerBid().getActualWin().getLoginName();
			this.actualPrice = product.getActualPrice();
		} else {
			this.winnerBid = messages.get("noBids");
			this.actualPrice = product.getLaunchPrice();
		}
		if (product.getTimeRemaining() < 0) {
			this.timeRemaining = messages.get("finalized");
		} else {
			this.timeRemaining = String.valueOf(product.getTimeRemaining());
		}

	}

	public int getRowsPerPage() {
		return ROWS_PER_PAGE;
	}

	void onActivate() {

		this.userId = userSession.getUserProfileId();

		productGridDataSource = new ProductGridDataSource(productService, userId);

	}

	Object[] onPassivate() {
		return new Object[] { userId };
	}

}
