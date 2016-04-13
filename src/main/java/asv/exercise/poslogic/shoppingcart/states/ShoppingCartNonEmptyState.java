package asv.exercise.poslogic.shoppingcart.states;

import asv.exercise.poslogic.shoppingcart.AbstractShoppingCartContext;

/**
 * Created by javier on 08/01/16.
 */
public class ShoppingCartNonEmptyState extends AbstractShoppingCartState {

    @Override
    public AbstractShoppingCartState handle( AbstractShoppingCartContext shoppingCart ) {

        AbstractShoppingCartState newState = this;

        if ( shoppingCart.getPaymentFlag() ) {

            newState = new ShoppingCartPaymentConfirmationState();
            shoppingCart.setState( newState );

        }

        return newState;
    }

}
