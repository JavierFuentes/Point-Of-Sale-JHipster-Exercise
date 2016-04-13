package asv.exercise.poslogic.shoppingcart.details.items;

/**
 * Created by javier on 09/01/16.
 */
public class Item extends AbstractItem {

    // ********************************
    //         Template  Method
    //         *Design Pattern*
    // ********************************
    @Override
    public int summarizeQuantity() {
        return getQuantity();
    }

    @Override
    public float summarizeBaseAmount() {
        return getBaseAmount();
    }

    @Override
    public float summarizeDiscountAmount() {
        return getDiscountAmount();
    }

    @Override
    public float summarizeTaxAmount() {
        return getTaxAmount();
    }

    @Override
    public float summarizeTotalAmount() {
        return getTotalAmount();
    }

}
