package asv.exercise.poslogic.shoppingcart.details.items;

/**
 * Created by javier on 10/01/16.
 */
public abstract class AbstractItemList extends CompositeItem {

    // ********************************
    //         Template  Method
    //         *Design Pattern*
    // ********************************
    @Override
    public int summarizeQuantity() {
        int sum = 0;

        for ( AbstractItem element : this.getComponents() ) {
            sum += element.summarizeQuantity();
        }

        return sum;
    }

    @Override
    public float summarizeBaseAmount() {
        int sum = 0;

        for ( AbstractItem element : this.getComponents() ) {
            sum += element.summarizeBaseAmount();
        }

        return sum;
    }

    @Override
    public float summarizeDiscountAmount() {
        int sum = 0;

        for ( AbstractItem element : this.getComponents() ) {
            sum += element.summarizeDiscountAmount();
        }

        return sum;
    }

    @Override
    public float summarizeTaxAmount() {
        int sum = 0;

        for ( AbstractItem element : this.getComponents() ) {
            sum += element.summarizeTaxAmount();
        }

        return sum;
    }

    @Override
    public float summarizeTotalAmount() {
        int sum = 0;

        for ( AbstractItem element : this.getComponents() ) {
            sum += element.summarizeTotalAmount();
        }

        return sum;
    }

    @Override
    public boolean addItem( AbstractItem item ) throws Exception {
        boolean result;

        result = this.getComponents().add( item );
        calculateTotals();

        return result;
    }

    @Override
    public void calculateTotals() throws Exception {
        this.setPrice( 0 );
        this.setQuantity( 0 );
        this.setBaseAmount( 0 );
        this.setDiscountAmount( 0 );
        this.setTaxAmount( 0 );
        this.setTotalAmount( 0 );

        for ( AbstractItem element : this.getComponents() ) {
            this.setQuantity( this.getQuantity() + element.summarizeQuantity() );
            this.setBaseAmount( this.getBaseAmount() + element.summarizeBaseAmount() );

            this.setPrice( this.getBaseAmount() / this.getQuantity() );  // average price

            this.setDiscountAmount( this.getDiscountAmount() + element.summarizeDiscountAmount() );
            this.setTaxAmount( this.getTaxAmount() + element.summarizeTaxAmount() );
            this.setTotalAmount( this.getTotalAmount() + element.summarizeTotalAmount() );
        }
    }

}
