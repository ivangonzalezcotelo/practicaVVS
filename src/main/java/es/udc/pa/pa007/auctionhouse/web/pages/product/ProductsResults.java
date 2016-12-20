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

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class ProductsResults {

	@ActivationRequestParameter(value = "prodName")
	private String prodName;

	@Property
	@ActivationRequestParameter(value = "catId")
	private Long catId;

	private Product product;

	@Inject
	private Messages messages;

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	@Inject
	private ProductService productService;

	@Property
	private SearchProductGridDataSource source;

	@InjectComponent
	private Grid grid;

	@Inject
	private BeanModelSource beanModelSource;

	@Property
	private BeanModel<Product> myModel;

	@Inject
	private Locale locale;

	public Format getFormat() {
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		return formatter;
	}

	public Link set(String prodName, Long catId) {
		this.prodName = prodName;
		this.catId = catId;

		return pageRenderLinkSource.createPageRenderLink(this.getClass());
	}

	void onActivate() {
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
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
		if (product.getWinnerBid() == null) {
			product.setActualPrice(product.getLaunchPrice());
		}
	}
}
