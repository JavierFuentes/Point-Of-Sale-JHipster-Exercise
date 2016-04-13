package asv.exercise.poslogic.shoppingcart.details.discounts;

import asv.exercise.poslogic.shoppingcart.details.items.AbstractItem;

/**
 * Created by javier on 09/01/16.
 */
public class DiscountCalculatorContext {

    // TODO: Pendiente revisar este patrón y terminar de ver si vale o no y como encaja en el modelo

    // ********************************
    //             Strategy
    //         *Design Pattern*
    // ********************************
    private AbstractDiscountStrategy discountCalculator = null;

    public AbstractDiscountStrategy getDiscountCalculator() {
        return discountCalculator;
    }

    public void setDiscountCalculator( AbstractDiscountStrategy discountCalculator ) {
        this.discountCalculator = discountCalculator;
    }

    public float getDiscount( AbstractItem item ) throws Exception {
        if ( discountCalculator == null ) {
            throw new Exception( "Not set discountCalculator instance" );
        }

        return discountCalculator.calculateDiscount( item );
    }

}
