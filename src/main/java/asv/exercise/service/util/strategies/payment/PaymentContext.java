package asv.exercise.service.util.strategies.payment;

/**
 * Created by javierfuentes on 22/02/2016.
 */
public class PaymentContext {

    private PaymentStrategy paymentStrategy = null;

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public void setPaymentStrategy( Long paymentMethodId ) throws Exception {

        switch ( PaymentMethodsAccepted.get(paymentMethodId) ) {
            case CASH:
                this.paymentStrategy = new PayByCash();
                break;

            case CREDITCARD:
                this.paymentStrategy = new PayByCreditCard();
                break;

            default:
                throw new Exception(  "SetPaymentStrategy( " + paymentMethodId.toString() + " ) not implemented yet!" );
        }

    }

    public String register() {

        return this.paymentStrategy.registerPayment();

    }
}
