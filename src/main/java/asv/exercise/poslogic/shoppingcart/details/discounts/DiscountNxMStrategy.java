package asv.exercise.poslogic.shoppingcart.details.discounts;

import asv.exercise.poslogic.shoppingcart.details.items.AbstractItem;

/**
 * Created by javier on 09/01/16.
 */
public class DiscountNxMStrategy extends AbstractDiscountStrategy {

    @Override
    public float calculateDiscount( AbstractItem item) {

        int quantityFree = (item.getQuantity() / this.quantityN ) * (this.quantityN - this.quantityM);
        return quantityFree * item.getPrice();

    }

}
