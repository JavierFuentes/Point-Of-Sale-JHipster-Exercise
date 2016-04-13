package asv.exercise.poslogic.shoppingcart.details.discounts;

import asv.exercise.poslogic.shoppingcart.details.items.AbstractItem;

/**
 * Created by javier on 09/01/16.
 */
public abstract class AbstractDiscountStrategy {

    // ********************************
    //             Strategy
    //         *Design Pattern*
    // ********************************
    protected float percentage = 0;
    protected int quantityN = 0;
    protected int quantityM = 0;

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage( float percentage ) throws Exception {
        if ( percentage < 0 ) {
            throw new Exception( "Percentage discount can't be negative (" + percentage + ")" );
        }

        this.percentage = percentage;
    }

    public int[] getNxM() {
        int[] values = new int[ 2 ];
        values[ 0 ] = quantityN;
        values[ 1 ] = quantityM;

        return values;
    }

    public void setNxM( int[] values ) throws Exception {
        if ( values.length < 2 ) {
            throw new Exception( "Array discount must have size 2 (" + values.length + ")" );
        }

        if ( ( values[ 0 ] <= 0 ) || ( values[ 1 ] <= 0 ) ) {
            throw new Exception( "Array discount must have positive values (" + values[ 0 ] + ", " + values[ 1 ] + ")" );
        }

        this.quantityN = values[ 0 ];
        this.quantityM = values[ 1 ];
    }

    public abstract float calculateDiscount( AbstractItem item );

}
