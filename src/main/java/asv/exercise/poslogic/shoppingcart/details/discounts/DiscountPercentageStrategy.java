package asv.exercise.poslogic.shoppingcart.details.discounts;

import asv.exercise.poslogic.shoppingcart.details.items.AbstractItem;

/**
 * Created by javier on 09/01/16.
 */
public class DiscountPercentageStrategy extends AbstractDiscountStrategy {

    @Override
    public float calculateDiscount( AbstractItem item ) {
        return item.getBaseAmount() * this.percentage / 100;
    }
}
