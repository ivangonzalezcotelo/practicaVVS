package es.udc.pa.pa007.auctionhouse.model.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Immutable;
import static es.udc.pa.pa007.auctionhouse.model.util.GlobalNames.PAGINATION;

/**
 * Category.
 *
 */
@Entity
@Immutable
@BatchSize(size = PAGINATION)
public class Category {

	/**
	 * The category Id.
	 */
	private Long catId;

	/**
	 * The category name.
	 */
	private String catName;

	/**
	 * Instance.
	 */
	public Category() {
	}

	/**
	 * @param catName
	 *            the category name.
	 */
	public Category(String catName) {

		/**
		 * NOTE: "catId" *must* be left as "null" since its value is
		 * automatically generated.
		 */
		this.catName = catName;
	}

	/**
	 * @return the category Id.
	 */
	@Column(name = "catId")
	@SequenceGenerator(// It only takes effect for
			name = "CategoryIdGenerator", // databases providing identifier
			sequenceName = "CategorySeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CategoryIdGenerator")
	public Long getCatId() {
		return catId;
	}

	/**
	 * @param productId
	 *            the prodctId.
	 */
	public void setCatId(long productId) {
		this.catId = productId;
	}

	/**
	 * @return the category name.
	 */
	public String getCatName() {
		return catName;
	}

	/**
	 * @param catName the category name.
	 */
	public void setCatName(String catName) {
		this.catName = catName;
	}
}
