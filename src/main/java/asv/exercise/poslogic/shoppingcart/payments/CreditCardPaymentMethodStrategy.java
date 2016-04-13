package asv.exercise.poslogic.shoppingcart.payments;

import asv.exercise.poslogic.shoppingcart.AbstractShoppingCartContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by javier on 09/01/16.
 */
public class CreditCardPaymentMethodStrategy extends AbstractPaymentMethodStrategy {

    private static final Logger log = LoggerFactory.getLogger( PaymentManagerContext.class );

    private String creditCard;

    public void setCreditCard( String creditCard ) {
        this.creditCard = creditCard;
    }

    @Override
    public boolean doPayment( AbstractShoppingCartContext shoppingCart, float money ) throws Exception {
        boolean result = false;

        if ( money != shoppingCart.getCart().getTotalAmount() ) {
            throw new Exception( "Can't doPayment with credit card: money must be " + shoppingCart.getCart().getTotalAmount() );
        }

        result = connectWithBank( money, this.creditCard );

        return result;
    }

    // TODO: Connect with Bank account and wait for transaction confirmation
    public boolean connectWithBank( float money, String creditcard ) {
        // Simulates external connection time
        try {
            wait( 5000 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }

        log.debug( "take money: " + money );

        double randomResponse = Math.random();

        return (randomResponse >= 0.5);
    }

}
