package es.udc.pa.pa007.auctionhouse.model.bid;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.tapestry5.beaneditor.NonVisual;

import es.udc.pa.pa007.auctionhouse.model.product.Product;
import es.udc.pa.pa007.auctionhouse.model.userprofile.UserProfile;

/**
 * Clase Bid.
 *
 */
@Entity
public class Bid {

	/**
	 * The bidId.
	 */
	@NonVisual
	private Long bidId;

	/**
	 * The Product.
	 */
	private Product productId;

	/**
	 * The UserProfile.
	 */
	private UserProfile userId;

	/**
	 * The currentValue.
	 */
	private BigDecimal currentValue;

	/**
	 * The maxBid.
	 */
	private BigDecimal maxBid;

	/**
	 * The Date.
	 */
	private Calendar bidDate;

	/**
	 * The actualWinner.
	 */
	private UserProfile actualWin;

	/**
	 * Constructor.
	 */
	public Bid() {
	}

	/**
	 * Constructor a partir de datos.
	 * 
	 * @param prodId
	 *            el producto sobre el que se puja
	 * @param usrId
	 *            el usuario que hace la puja
	 * @param currentValue
	 *            el valor actual
	 * @param maxBid
	 *            el valor de la puja maxima
	 * @param bidDate
	 *            la fecha de la puja
	 * @param actualWin
	 *            el ganador actual
	 */
	public Bid(Product prodId, UserProfile usrId, BigDecimal currentValue, BigDecimal maxBid, Calendar bidDate,
			UserProfile actualWin) {
		/**
		 * NOTE: "bidId" *must* be left as "null" since its value is
		 * automatically generated.
		 */
		this.productId = prodId;
		this.userId = usrId;
		this.currentValue = currentValue;
		this.maxBid = maxBid;
		this.bidDate = bidDate;
		this.actualWin = actualWin;

	}

	/**
	 * @return the BidId.
	 */
	@Column(name = "bidId")
	@SequenceGenerator(// It only takes effect for
			name = "BidIdGenerator", // databases providing identifier
			sequenceName = "BidSeq") // generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "BidIdGenerator")
	public Long getBidId() {
		return bidId;
	}

	/**
	 * @param bidId The bidId.
	 */
	public void setBidId(long bidId) {
		this.bidId = bidId;
	}

	/**
	 * @return the Product.
	 */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public Product getProductId() {
		return productId;
	}

	/**
	 * @param prodId The Product.
	 */
	public void setProductId(Product prodId) {
		this.productId = prodId;
	}

	/**
	 * @return The currentValue. 
	 */
	public BigDecimal getCurrentValue() {
		return currentValue;
	}

	/**
	 * @param currentValue The currentValue.
	 */
	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}

	/**
	 * @return The MaxBid.
	 */
	public BigDecimal getMaxBid() {
		return maxBid;
	}

	/**
	 * @param maxBid The MaxBid.
	 */
	public void setMaxBid(BigDecimal maxBid) {
		this.maxBid = maxBid;
	}

	/**
	 * @return The BidDate.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getBidDate() {
		return bidDate;
	}

	/**
	 * @param bidDate The BidDate.
	 */
	public void setBidDate(Calendar bidDate) {
		this.bidDate = bidDate;
	}

	/**
	 * @return The ActualWinner.
	 */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "actualWinner")
	public UserProfile getActualWin() {
		return actualWin;
	}

	/**
	 * @param actualWin The ActualWinner.
	 */
	public void setActualWin(UserProfile actualWin) {
		this.actualWin = actualWin;
	}

	/**
	 * @return The UserProfile.
	 */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public UserProfile getUserId() {
		return userId;
	}

	/**
	 * @param usrId The UserProfile.
	 */
	public void setUserId(UserProfile usrId) {
		this.userId = usrId;
	}
}
