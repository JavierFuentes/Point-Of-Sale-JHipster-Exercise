package asv.exercise.poslogic.shoppingcart.payments;

import asv.exercise.poslogic.shoppingcart.AbstractShoppingCartContext;

/**
 * Created by javier on 09/01/16.
 */
public abstract class AbstractPaymentMethodStrategy {

    // ********************************
    //             Strategy
    //         *Design Pattern*
    // ********************************

    public abstract boolean doPayment( AbstractShoppingCartContext shoppingCart, float money ) throws Exception;

}
