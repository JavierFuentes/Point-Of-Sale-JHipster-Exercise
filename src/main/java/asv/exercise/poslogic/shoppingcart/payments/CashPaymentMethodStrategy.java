package asv.exercise.poslogic.shoppingcart.payments;

import asv.exercise.poslogic.shoppingcart.AbstractShoppingCartContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by javier on 09/01/16.
 */
public class CashPaymentMethodStrategy extends AbstractPaymentMethodStrategy {

    private static final Logger log = LoggerFactory.getLogger( PaymentManagerContext.class );

    @Override
    public boolean doPayment( AbstractShoppingCartContext shoppingCart, float money ) throws Exception {
        boolean result = false;

        if ( money < shoppingCart.getCart().getTotalAmount() ) {
            throw new Exception( "Can't doPayment with cash: money must be at least " + shoppingCart.getCart().getTotalAmount() );
        }

        log.debug( "take money: " + money );

        double randomResponse = Math.random();

        return (randomResponse >= 0.5);
    }

}
