package es.udc.pa.pa007.auctionhouse.model.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@BatchSize(size=10)
public class Category {
	private Long catId;
	private String catName;
	
	public Category(){
	}
	
	public Category(String catName){
		
		/**
		 * NOTE: "catId" *must* be left as "null" since its value is
		 * automatically generated.
		 */
		this.catName = catName;
	}
	
	@Column(name = "catId")
	@SequenceGenerator( // It only takes effect for
	name = "CategoryIdGenerator", // databases providing identifier
	sequenceName = "CategorySeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CategoryIdGenerator")
	public Long getCatId() {
		return catId;
	}

	public void setCatId(long productId) {
		this.catId = productId;
	}
	
	public String getCatName(){
		return catName;		
	}
	
	public void setCatName(String catName){
		this.catName = catName;
	}
}
