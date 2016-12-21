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

/**
 * OwnProducts.
 *
 */
public class OwnProducts {

	/**
	 * The rows per page.
	 */
	private final int rowsPerPage = 4;

	/**
	 * The userProfile Id.
	 */
	private Long userId;
	/**
	 * The ProductGridDataSource.
	 */
	private ProductGridDataSource productGridDataSource;
	/**
	 * The Product.
	 */
	private Product product;

	/**
	 * The winner bid.
	 */
	@Property
	private String winnerBid;

	/**
	 * The current price.
	 */
	@Property
	private BigDecimal actualPrice;

	/**
	 * The remaining time.
	 */
	@Property
	private String timeRemaining;

	/**
	 * The Messages.
	 */
	@Inject
	private Messages messages;

	/**
	 * The UserSession.
	 */
	@SessionState(create = false)
	private UserSession userSession;

	/**
	 * The ProductService.
	 */
	@Inject
	private ProductService productService;

	/**
	 * The Locale.
	 */
	@Inject
	private Locale locale;

	/**
	 * @return the format.
	 */
	public Format getFormat() {
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		return formatter;
	}

	/**
	 * @param userId
	 *            the userProfile Id.
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the ProductGridDataSource.
	 */
	public ProductGridDataSource getProductGridDataSource() {
		return productGridDataSource;
	}

	/**
	 * @return the Product.
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            the Product.
	 */
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

	/**
	 * @return the rows per page.
	 */
	public int getRowsPerPage() {
		return rowsPerPage;
	}

	/**
	 * The onActivate.
	 */
	public void onActivate() {

		this.userId = userSession.getUserProfileId();

		productGridDataSource = new ProductGridDataSource(productService, userId);

	}

	/**
	 * @return the Object.
	 */
	public Object[] onPassivate() {
		return new Object[] { userId };
	}

}
