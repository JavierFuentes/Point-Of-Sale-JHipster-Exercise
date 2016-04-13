package asv.exercise.poslogic.shoppingcart;

import asv.exercise.poslogic.shoppingcart.details.items.AbstractItem;
import asv.exercise.poslogic.shoppingcart.details.items.AbstractItemList;
import asv.exercise.poslogic.shoppingcart.details.items.CartItemList;
import asv.exercise.poslogic.shoppingcart.details.items.CompositeItem;
import asv.exercise.poslogic.shoppingcart.states.AbstractShoppingCartState;
import asv.exercise.poslogic.shoppingcart.states.ShoppingCartInitialState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by javier on 08/01/16.
 */
public abstract class AbstractShoppingCartContext {

    private static final Logger log = LoggerFactory.getLogger( AbstractShoppingCartContext.class );

    // *******************************
    // Fields and Properties
    // *******************************
    private boolean paymentFlag = false;
    private boolean paymentSuccessFlag = false;
    private AbstractItemList cart = new CartItemList();

    public boolean getPaymentFlag() {
        return paymentFlag;
    }

    public void setPaymentFlag( boolean paymentFlag ) {
        this.paymentFlag = paymentFlag;
    }

    public boolean getPaymentSuccessFlag() {
        return paymentSuccessFlag;
    }

    public void setPaymentSuccessFlag( boolean paymentSuccessFlag ) {
        this.paymentSuccessFlag = paymentSuccessFlag;
    }

    public CompositeItem getCart() {
        return cart;
    }

    protected void setCart( AbstractItemList cart ) {
        this.cart = cart;
    }

    // *******************************
    // Methods
    // *******************************
    public AbstractItemList addToCart( AbstractItem item, int quantity ) throws Exception {

        log.debug( "addToCart {} units of item {}", quantity, item.getProductId() );

        item.addQuantity( quantity );

        boolean alreadyExist = false;

        for ( AbstractItem element : this.cart.getComponents() ) {

            // Accumulate over productId and sign of quantity
            if ( ( element.getProductId().equals( item.getProductId() ) ) && ( element.isRefund() == item.isRefund() ) ) {
                element.addQuantity( quantity );

                if ( element.getQuantity() == 0 ) {

                    this.cart.removeItem( element );

                } else {

                    // Update Amount fields at element
                    element.calculateTotals();

                }

                alreadyExist = true;
                break;
            }
        }

        if ( !alreadyExist ) {
            // Update Amount fields at item before it will be added to cart
            item.calculateTotals();

            this.cart.addItem( item );
        }

        // Update cart total Amount fields
        this.cart.calculateTotals();

        // Update the state if proceed
        this.handleState();

//        log.debug( "addToCart List of Items in cart {}", this.cart.get() );
        log.debug( "Cart Total Amounts and Item List {}", this.cart);

        return this.cart;
    }

    // *******************************
    //             State
    //         *Design Pattern*
    // *******************************
    private AbstractShoppingCartState state;

    public AbstractShoppingCartContext( AbstractShoppingCartState state ) {
        this.setState( state );
    }

    public final AbstractShoppingCartState getState() {
        return state;
    }

    public final void setState( AbstractShoppingCartState state ) {

        // Init all
        if ( state instanceof ShoppingCartInitialState ) {
            this.setCart( new CartItemList() );
        }

        this.state = state;
    }

    public final AbstractShoppingCartState handleState() {
        return state.handle( this );
    }

}
