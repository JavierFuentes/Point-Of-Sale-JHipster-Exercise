package asv.exercise.poslogic.shoppingcart.states;

import asv.exercise.poslogic.shoppingcart.AbstractShoppingCartContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by javier on 08/01/16.
 */
public abstract class AbstractShoppingCartState {

    private static final Logger log = LoggerFactory.getLogger( AbstractShoppingCartState.class );

    public AbstractShoppingCartState() {
        log.debug( "Shopping Cart actual State {}", this.getClass().getSimpleName() );
    }

    // *******************************
    //             State
    //         *Design Pattern*
    // *******************************
    public abstract AbstractShoppingCartState handle( AbstractShoppingCartContext shoppingCartStateContext );

}
