//package asv.exercise.poslogic;
//
//import asv.exercise.poslogic.shoppingcart.AbstractShoppingCartContext;
//import asv.exercise.poslogic.shoppingcart.ShoppingCart;
//import asv.exercise.poslogic.shoppingcart.details.items.AbstractItem;
//import asv.exercise.poslogic.shoppingcart.details.items.CompositeItem;
//import asv.exercise.poslogic.shoppingcart.details.items.Item;
//import asv.exercise.poslogic.shoppingcart.payments.CashPaymentMethodStrategy;
//import asv.exercise.poslogic.shoppingcart.payments.PaymentManagerContext;
//import asv.exercise.poslogic.shoppingcart.states.ShoppingCartInitialState;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Created by javier on 08/01/16.
// */
//public class Client {
//
//    private static final Logger log = LoggerFactory.getLogger( Client.class );
//
//    private static AbstractShoppingCartContext shoppingCart;
//    private static AbstractItem[] item = new AbstractItem[10];
//    private static CompositeItem[] compositeItem = new CompositeItem[10];
//
//    public static void main( String[] args ) throws Exception {
//
//        log.debug( "Point Of Sale Example Design Pattern Client Tests" );
//
//        // Prepare data
//        prepareData();
//
//        // Create a brand new shopping cart
//        shoppingCart = new ShoppingCart( new ShoppingCartInitialState() );
//
//        // Add items to cart
//        shoppingCart.addToCart( item[ 0 ], 5 );
//        shoppingCart.addToCart( item[ 1 ], 3 );
//        shoppingCart.addToCart( item[ 1 ], -1 );  // Its own detail
//        shoppingCart.addToCart( item[ 1 ], -1 );  // Aggregate to existing detail
//        shoppingCart.addToCart( item[ 0 ], 2 );   // Aggregate to existing detail
//        shoppingCart.addToCart( compositeItem[ 0 ], 1 );
//
//        // Payment
//        log.debug( "\n\nShopping Cart before payment: " + shoppingCart.getCart() );
//        PaymentManagerContext paymentManager = new PaymentManagerContext();
//        paymentManager.setStrategy( new CashPaymentMethodStrategy() );
//        paymentManager.registerPayment( shoppingCart, 0 );
//        log.debug( "\nShopping Cart after payment: " + shoppingCart.getCart() );
//
//        ////// NEW CART
//
//
//    }
//
//    // Data for testing
//    private static void prepareData() throws Exception {
//
//        item[ 0 ] = new Item();
//        item[ 0 ].setProductId( "1" );
//        item[ 0 ].setDescription( "Product 1" );
//        item[ 0 ].setCategoryId( "Cat1" );
//        item[ 0 ].setDiscountId( "3x2" );
//        item[ 0 ].setTaxId( "VAT10" );
//        item[ 0 ].setPrice( 10.0F );
//
//        item[ 1 ] = new Item();
//        item[ 1 ].setProductId( "2" );
//        item[ 1 ].setDescription( "Product 2" );
//        item[ 1 ].setCategoryId( "Cat2" );
//        item[ 1 ].setDiscountId( "10%" );
//        item[ 0 ].setTaxId( "VAT4" );
//        item[ 1 ].setPrice( 5.0F );
//
//        // Add a Composite Item
//        compositeItem[ 0 ] = new CompositeItem();
//        compositeItem[ 0 ].setProductId( "C1" );
//        compositeItem[ 0 ].setDescription( "Product C1" );
//        compositeItem[ 0 ].setCategoryId( "Cat3" );
//        compositeItem[ 0 ].setDiscountId( "10%" );
//        compositeItem[ 0 ].setTaxId( "VAT10" );
//        compositeItem[ 0 ].setPrice( 20.0F );
//
////        item = new Item();
//        item[ 0 ].addQuantity( 2 );
//        compositeItem[ 0 ].addItem( item[ 0 ] );
//
//        item[ 1 ].addQuantity( 3 );
//        compositeItem[ 0 ].addItem( item[ 1 ] );
//
//    }
//}