package asv.exercise.poslogic.shoppingcart.payments;

import asv.exercise.poslogic.shoppingcart.AbstractShoppingCartContext;
import asv.exercise.poslogic.shoppingcart.states.ShoppingCartPaymentConfirmationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by javier on 09/01/16.
 */
public class PaymentManagerContext {

    private static final Logger log = LoggerFactory.getLogger( PaymentManagerContext.class );

    private String creditCard = "";

    public void setCreditCard( String creditCard ) {
        this.creditCard = creditCard;
    }

    // ********************************
    //             Strategy
    //         *Design Pattern*
    // ********************************

    private AbstractPaymentMethodStrategy strategy = null;

    public AbstractPaymentMethodStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy( AbstractPaymentMethodStrategy strategy ) {
        this.strategy = strategy;
    }

    public boolean registerPayment( AbstractShoppingCartContext shoppingCart, float money ) throws Exception {
        boolean paymentSuccess = false;

        if ( strategy == null ) {
            throw new Exception( "Not set payment method strategy instance" );
        }

        shoppingCart.setPaymentFlag( true );
        shoppingCart.setPaymentSuccessFlag( false );
        shoppingCart.handleState();  // update to waiting payment confirmation

        if ( shoppingCart.getState().getClass() != ShoppingCartPaymentConfirmationState.class ) {
            log.error( "Can't registerPayment. Shopping Cart actual State {}", shoppingCart.getState().getClass() );
        }
        else {

            if ( strategy instanceof CreditCardPaymentMethodStrategy ) {

                ( (CreditCardPaymentMethodStrategy) strategy ).setCreditCard( this.creditCard );

                if ( money <= 0 ) {
                    money = shoppingCart.getCart().getTotalAmount();
                }

            }
            else {

                if ( money <= 0 ) {
                    money = (float) Math.floor( ( shoppingCart.getCart().getTotalAmount() + ( (1 + Math.random() * 10 )) ));
                }

            }

            paymentSuccess = this.strategy.doPayment( shoppingCart, money );

        }

        log.debug( "registerPayment " + this.strategy.getClass().getSimpleName() + " result: " + paymentSuccess );

        shoppingCart.setPaymentSuccessFlag( paymentSuccess );
        shoppingCart.handleState();  // update to success o failure

        // TODO: Grabar la transacción en la BD
        shoppingCart.handleState();  // saving DB o cart non-empty
        //saveTransaction();
        shoppingCart.handleState();  // initial state o payment confirmation

        return paymentSuccess;
    }

}
