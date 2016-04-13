package asv.exercise.service.util.strategies.payment;

/**
 * Created by javier on 13/01/16.
 */
public enum PaymentMethodsAccepted {

    CASH( 1 ),
    CREDITCARD( 2 );

    private final int id;

    PaymentMethodsAccepted( int id ) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PaymentMethodsAccepted get( Long id ) throws Exception {

        int _id = (int) ( (long) id );

        switch ( _id ) {
            case 1:
                return CASH;

            case 2:
                return CREDITCARD;

            default:
                throw new Exception( id.toString() + " PaymentMethodsAccepted Enum not valid" );
        }

    }


}
