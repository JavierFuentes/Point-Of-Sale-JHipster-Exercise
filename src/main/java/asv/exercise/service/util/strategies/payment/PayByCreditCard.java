package asv.exercise.service.util.strategies.payment;

/**
 * Created by javierfuentes on 22/02/2016.
 */
public class PayByCreditCard implements PaymentStrategy {

    @Override
    public String registerPayment() {

        // Simulates error with Credit Card: no credit, wrong pin, break card...
        double rnd = Math.random();
        if ( rnd < 0.5 )
            return "CREDITCARD_ERROR";

        // Simulates authorization received from Bank
        Long authorization = (long) (Math.random() * 1E15);
        return authorization.toString();

    }

}
