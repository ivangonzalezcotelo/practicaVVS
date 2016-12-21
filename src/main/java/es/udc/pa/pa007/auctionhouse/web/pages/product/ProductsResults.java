package es.udc.pa.pa007.auctionhouse.web.pages.product;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.PageRenderLinkSource;

import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.productservice.ProductService;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicy;
import es.udc.pa.pa007.auctionhouse.web.services.AuthenticationPolicyType;
import es.udc.pa.pa007.auctionhouse.web.util.SearchProductGridDataSource;

/**
 * ProductResults.
 *
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class ProductsResults {

	/**
	 * The product name.
	 */
	@ActivationRequestParameter(value = "prodName")
	private String prodName;

	/**
	 * The category Id.
	 */
	@Property
	@ActivationRequestParameter(value = "catId")
	private Long catId;

	/**
	 * The Product.
	 */
	private Product product;

	/**
	 * The Messages.
	 */
	@Inject
	private Messages messages;

	/**
	 * The PageRenderLinkSource.
	 */
	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	/**
	 * The ProductService.
	 */
	@Inject
	private ProductService productService;

	/**
	 * The source.
	 */
	@Property
	private SearchProductGridDataSource source;

	/**
	 * The Grid.
	 */
	@InjectComponent
	private Grid grid;

	/**
	 * The BeanModelSource.
	 */
	@Inject
	private BeanModelSource beanModelSource;

	/**
	 * The myModel.
	 */
	@Property
	private BeanModel<Product> myModel;

	/**
	 * The Locale.
	 */
	@Inject
	private Locale locale;

	/**
	 * @return the Format.
	 */
	public Format getFormat() {
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		return formatter;
	}

	/**
	 * @param prodName
	 *            the product name.
	 * @param catId
	 *            the category Id.
	 * @return the Link.
	 */
	public Link set(String prodName, Long catId) {
		this.prodName = prodName;
		this.catId = catId;

		return pageRenderLinkSource.createPageRenderLink(this.getClass());
	}

	/**
	 * The onActivate.
	 */
	public void onActivate() {
		this.source = new SearchProductGridDataSource(productService, catId, prodName);
		myModel = beanModelSource.createDisplayModel(Product.class, messages);
		myModel.get("actualPrice").sortable(false);
		myModel.get("prodName").sortable(false);
		myModel.get("launchPrice").sortable(false);
		myModel.get("timeRemaining").sortable(false);
	}

	/*
	 * void setupRender() { myModel =
	 * beanModelSource.createDisplayModel(Product.class, messages);
	 * myModel.get("actualPrice").sortable(false);
	 * myModel.get("prodName").sortable(false);
	 * myModel.get("launchPrice").sortable(false);
	 * myModel.get("timeRemaining").sortable(false); }
	 */
	/**
	 * @return the Product.
	 */
	public Product getProduct() {
		return this.product;
	}

	/**
	 * @param product
	 *            the product
	 */
	public void setProduct(Product product) {
		this.product = product;
		if (product.getWinnerBid() == null) {
			product.setActualPrice(product.getLaunchPrice());
		}
	}
}
