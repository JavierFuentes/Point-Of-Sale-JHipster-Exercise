package asv.exercise.service.util.strategies.payment;

/**
 * Created by javierfuentes on 22/02/2016.
 */
public interface PaymentStrategy {

    // Must return a payment authorization id
    public String registerPayment();

}
