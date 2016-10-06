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

@Entity
public class Bid {
	@NonVisual
	private Long bidId;
	private Product productId;
	private UserProfile userId;
	private BigDecimal currentValue;
	private BigDecimal maxBid;
	private Calendar bidDate;
	private UserProfile actualWin;
	
	public Bid (){
	}
	
	public Bid(Product prodId, UserProfile usrId, BigDecimal currentValue,
			   BigDecimal maxBid, Calendar bidDate, UserProfile actualWin) {
		/**
		 * NOTE: "bidId" *must* be left as "null" since its value is
		 * automatically generated.
		 */		
		this.productId = prodId;
		this.userId  = usrId;
		this.currentValue = currentValue;
		this.maxBid = maxBid;
		this.bidDate = bidDate;
		this.actualWin = actualWin;
		
	}
	
	@Column(name = "bidId")
	@SequenceGenerator(      // It only takes effect for
	name = "BidIdGenerator", // databases providing identifier
	sequenceName = "BidSeq") // generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "BidIdGenerator")
	public Long getBidId(){
		return bidId;
	}
	
	public void setBidId(long bidId) {
		this.bidId = bidId;
	}
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="productId")
	public Product getProductId(){
		return productId;			
	}
	
	public void setProductId(Product prodId){
		this.productId = prodId;
	}

	public BigDecimal getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}

	public BigDecimal getMaxBid() {
		return maxBid;
	}

	public void setMaxBid(BigDecimal maxBid) {
		this.maxBid = maxBid;
	}

	@Temporal(TemporalType.TIMESTAMP)	
	public Calendar getBidDate() {
		return bidDate;
	}

	public void setBidDate(Calendar bidDate) {
		this.bidDate = bidDate;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="actualWinner")
	public UserProfile getActualWin() {
		return actualWin;
	}

	public void setActualWin(UserProfile actualWin) {
		this.actualWin = actualWin;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	public UserProfile getUserId() {
		return userId;
	}

	public void setUserId(UserProfile usrId) {
		this.userId = usrId;
	}
}
