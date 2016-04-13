package asv.exercise.service.util.strategies.payment;

/**
 * Created by javierfuentes on 22/02/2016.
 */
public class PayByCash implements PaymentStrategy {

    @Override
    public String registerPayment() {

        // Cash is always OK
        return "PAYMENT_BY_CASH_OK";

    }

}
