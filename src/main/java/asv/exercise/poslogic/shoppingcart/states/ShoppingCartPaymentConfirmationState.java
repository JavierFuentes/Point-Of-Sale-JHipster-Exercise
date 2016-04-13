package asv.exercise.poslogic.shoppingcart.states;

import asv.exercise.poslogic.shoppingcart.AbstractShoppingCartContext;

/**
 * Created by javier on 08/01/16.
 */
public class ShoppingCartPaymentConfirmationState extends AbstractShoppingCartState {

    @Override
    public AbstractShoppingCartState handle( AbstractShoppingCartContext shoppingCart ) {


        // TODO: Diferenciar el Pago con éxito del error en la transacción
        AbstractShoppingCartState newState;

        if ( shoppingCart.getPaymentSuccessFlag() ) {
            newState = new ShoppingCartPaymentSuccessState();
        }
        else {
            newState = new ShoppingCartPaymentFailureState();
        }

        shoppingCart.setState( newState );

        return newState;
    }


}
