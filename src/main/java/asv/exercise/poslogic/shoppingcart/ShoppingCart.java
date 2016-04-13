package asv.exercise.poslogic.shoppingcart;

import asv.exercise.poslogic.shoppingcart.states.AbstractShoppingCartState;

/**
 * Created by javier on 08/01/16.
 */
public class ShoppingCart extends AbstractShoppingCartContext {

    public ShoppingCart( AbstractShoppingCartState state ) {
        super( state );
    }

}
