package asv.exercise.poslogic.shoppingcart.states;

import asv.exercise.poslogic.shoppingcart.AbstractShoppingCartContext;

/**
 * Created by javier on 08/01/16.
 */
public class ShoppingCartSavingTransactionState extends AbstractShoppingCartState {

    @Override
    public AbstractShoppingCartState handle( AbstractShoppingCartContext shoppingCart ) {

        // TODO: Gestionar la transacción en la BD para finalizar completamente la operación... sino, volver a intentarlo.

        AbstractShoppingCartState newState = new ShoppingCartInitialState();   // Previous state

        shoppingCart.setState( newState );

        return newState;
    }

}
