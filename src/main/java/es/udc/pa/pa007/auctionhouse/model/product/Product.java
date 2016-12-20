package es.udc.pa.pa007.auctionhouse.model.product;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.tapestry5.beaneditor.NonVisual;

import es.udc.pa.pa007.auctionhouse.model.bid.Bid;
import es.udc.pa.pa007.auctionhouse.model.category.Category;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;

/**
 * Product.
 *
 */
@Entity
public class Product {

	/**
	 * The time.
	 */
	private final int time = 60000;

	/**
	 * The product id.
	 */
	@NonVisual
	private Long prodId;
	/**
	 * The product name.
	 */
	private String prodName;
	/**
	 * The description.
	 */
	private String description;
	/**
	 * The launch price.
	 */
	private BigDecimal launchPrice;
	/**
	 * The send info.
	 */
	private String sendInfo;
	/**
	 * The create Date.
	 */
	private Calendar createDate;
	/**
	 * The finish Date.
	 */
	private Calendar finishDate;
	/**
	 * The current price.
	 */
	private BigDecimal actualPrice;
	/**
	 * The winner bid.
	 */
	private Bid winnerBid;
	/**
	 * The owner.
	 */
	private UserProfile owner;
	/**
	 * The category.
	 */
	private Category category;
	/**
	 * The version.
	 */
	@NonVisual
	private Long version;

	/**
	 * Instance.
	 */
	public Product() {
	}

	/**
	 * @param prodName
	 *            the product name.
	 * @param description
	 *            the descripction.
	 * @param sendInfo
	 *            the send info.
	 * @param launchPrice
	 *            the launch price.
	 * @param createDate
	 *            the create date.
	 * @param finishDate
	 *            the finish date.
	 * @param owner
	 *            the owner.
	 * @param category
	 *            the category.
	 */
	public Product(String prodName, String description, String sendInfo, BigDecimal launchPrice, Calendar createDate,
			Calendar finishDate, UserProfile owner, Category category) {

		/**
		 * NOTE: "productId" *must* be left as "null" since its value is
		 * automatically generated.
		 */

		this.prodName = prodName;
		this.description = description;
		this.sendInfo = sendInfo;
		this.launchPrice = launchPrice;
		this.createDate = createDate;
		this.finishDate = finishDate;
		this.owner = owner;
		this.category = category;

	}

	/**
	 * @return the product id.
	 */
	@Column(name = "prodId")
	@SequenceGenerator(// It only takes effect for
			name = "ProdIdGenerator", // databases providing identifier
			sequenceName = "ProductSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ProdIdGenerator")
	public Long getProdId() {
		return prodId;
	}

	/**
	 * @param productId
	 *            the product id.
	 */
	public void setProdId(Long productId) {
		this.prodId = productId;
	}

	/**
	 * @return the product name.
	 */
	public String getProdName() {
		return prodName;
	}

	/**
	 * @param prodName
	 *            the product name.
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	/**
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the send info.
	 */
	public String getSendInfo() {
		return sendInfo;
	}

	/**
	 * @param sendInfo
	 *            the send info.
	 */
	public void setSendInfo(String sendInfo) {
		this.sendInfo = sendInfo;
	}

	/**
	 * @return the launch price.
	 */
	public BigDecimal getLaunchPrice() {
		return launchPrice;
	}

	/**
	 * @param launchPrice
	 *            the launch price.
	 */
	public void setLaunchPrice(BigDecimal launchPrice) {
		this.launchPrice = launchPrice;
	}

	/**
	 * @return the create date.
	 */
	public Calendar getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the create date.
	 */
	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the finish date.
	 */
	public Calendar getFinishDate() {
		return finishDate;
	}

	/**
	 * @param finishDate
	 *            the finish date.
	 */
	public void setFinishDate(Calendar finishDate) {
		this.finishDate = finishDate;
	}

	/**
	 * @return the current price.
	 */
	@Column(name = "actualValue")
	public BigDecimal getActualPrice() {
		return actualPrice;
	}

	/**
	 * @param actualPrice
	 *            the current price.
	 */
	public void setActualPrice(BigDecimal actualPrice) {
		this.actualPrice = actualPrice;
	}

	/**
	 * @return the winner Bid.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "winnerBid")
	public Bid getWinnerBid() {
		return winnerBid;
	}

	/**
	 * @param winnerBid
	 *            the winner Bid.
	 */
	public void setWinnerBid(Bid winnerBid) {
		this.winnerBid = winnerBid;
	}

	/**
	 * @return the owner.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner")
	public UserProfile getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner.
	 */
	public void setOwner(UserProfile owner) {
		this.owner = owner;
	}

	/**
	 * @return the Category.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category")
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the Category.
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the version.
	 */
	@Version
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version.
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * @return the remaining time of the product.
	 */
	@Transient
	public int getTimeRemaining() {
		Calendar cal = Calendar.getInstance();
		int res = new Double((finishDate.getTimeInMillis() - cal.getTimeInMillis()) / time).intValue();
		return res;
	}
}
