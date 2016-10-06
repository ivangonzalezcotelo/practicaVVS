package es.udc.pa.pa007.auctionhouse.web.util;



import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;

import es.udc.pa.pa007.auctionhouse.model.category.Category;

public class CategoryEncoder implements ValueEncoder<Category> {

	private SelectModel categorysModel;

    public CategoryEncoder(SelectModel categorysModel) {
        this.categorysModel = categorysModel;
    }

    @Override
    public String toClient(Category value) {
        return String.valueOf(value.getCatId());
    }

    @Override
    public Category toValue(String id) {
    	Category cat = null;
		for (OptionModel option: categorysModel.getOptions()) {
			Category catOption = (Category) option.getValue();
			if (catOption.getCatId().equals(Long.parseLong(id))) {
				cat = catOption;
				break;
			}
		}
		return cat;
    }
}