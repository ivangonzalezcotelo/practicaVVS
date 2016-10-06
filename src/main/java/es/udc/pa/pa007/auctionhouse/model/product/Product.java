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

@Entity
public class Product {

	@NonVisual
	private Long prodId;
	private String prodName;
	private String description;
	private BigDecimal launchPrice;
	private String sendInfo;
	private Calendar createDate;
	private Calendar finishDate;
	private BigDecimal actualPrice;
	private Bid winnerBid;
	private UserProfile owner;
	private Category category;
	@NonVisual
	private Long version;

	public Product() {
	}

	public Product(String prodName, String description, String sendInfo,
			BigDecimal launchPrice, Calendar createDate, Calendar finishDate, UserProfile owner, Category category) {

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

	@Column(name = "prodId")
	@SequenceGenerator( // It only takes effect for
	name = "ProdIdGenerator", // databases providing identifier
	sequenceName = "ProductSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ProdIdGenerator")
	public Long getProdId() {
		return prodId;
	}

	public void setProdId(Long productId) {
		this.prodId = productId;
	}
	
	public String getProdName(){
		return prodName;
	}
	
	public void setProdName(String prodName){
		this.prodName = prodName;
	}

	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getSendInfo(){
		return sendInfo;
	}
	
	public void setSendInfo(String sendInfo){
		this.sendInfo = sendInfo;
	}
	
	public BigDecimal getLaunchPrice(){
		return launchPrice;
	}
	
	public void setLaunchPrice(BigDecimal launchPrice){
		this.launchPrice = launchPrice;
	}
	
	public Calendar getCreateDate(){
		return createDate;
	}
	
	public void setCreateDate(Calendar createDate){
		this.createDate = createDate;
	}

	public Calendar getFinishDate(){
		return finishDate;
	}
	
	public void setFinishDate(Calendar finishDate){
		this.finishDate = finishDate;
	}
	
	@Column(name = "actualValue")
	public BigDecimal getActualPrice(){
		return actualPrice;
	}
	
	public void setActualPrice(BigDecimal actualPrice){
		this.actualPrice = actualPrice;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "winnerBid")
	public Bid getWinnerBid(){
		return winnerBid;
	}
	
	public void setWinnerBid(Bid winnerBid){
		this.winnerBid = winnerBid;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner")
	public UserProfile getOwner(){
		return owner;
	}
	
	public void setOwner(UserProfile owner){
		this.owner = owner;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category")
	public Category getCategory(){
		return category;
	}
	
	public void setCategory(Category category){
		this.category = category;
	}
	
	@Version
	public Long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	@Transient
	public int getTimeRemaining(){
		Calendar cal = Calendar.getInstance();
		int res = new Double((finishDate.getTimeInMillis()-cal.getTimeInMillis())/60000).intValue();
		return res;
	}
}
