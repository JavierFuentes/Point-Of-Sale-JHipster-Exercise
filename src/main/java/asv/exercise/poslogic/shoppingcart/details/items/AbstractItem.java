package asv.exercise.poslogic.shoppingcart.details.items;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by javier on 09/01/16.
 */
public abstract class AbstractItem {

    private static final Logger log = LoggerFactory.getLogger( AbstractItem.class );

    // *******************************
    // Fields and Properties
    // *******************************
//    private boolean shoppingCartFlag = false;

    private String categoryId = "";
    private String productId = "";
    private String discountId = "";
    private String taxId = "";
    private String description = "";
    private boolean refund = false;
    private int quantity = 0;
    private float price = 0;
    private float baseAmount = 0;
    private float discountAmount = 0;
    private float taxAmount = 0;
    private float totalAmount = 0;

//    public boolean isShoppingCart() {
//        return shoppingCartFlag;
//    }
//
//    protected void setShoppingCartFlag( boolean shoppingCartFlag ) {
//        this.shoppingCartFlag = shoppingCartFlag;
//    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId( String categoryId ) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId( String productId ) {
        this.productId = productId;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId( String discountId ) {
        this.discountId = discountId;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId( String taxId ) {
        this.taxId = taxId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public boolean isRefund() {
        return refund;
    }

//    private void setRefund( boolean refund ) {
//        this.refund = refund;
//    }

    public int getQuantity() {
        return quantity;
    }

    protected void setQuantity( int quantity ) {
        this.quantity = quantity;
        this.refund = (this.quantity < 0);
    }

    public float getPrice() {
        return price;
    }

    public void setPrice( float price ) throws Exception {
        if ( price < 0 ) {
            throw new Exception( "Price can't be negative (" + price + ")" );
        }

        this.price = price;
    }

    public float getBaseAmount() {
        return baseAmount;
    }

    protected void setBaseAmount( float baseAmount ) {
        this.baseAmount = baseAmount;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    protected void setDiscountAmount( float discountAmount ) {
        this.discountAmount = discountAmount;
    }

    public float getTaxAmount() {
        return taxAmount;
    }

    protected void setTaxAmount( float taxAmount ) {
        this.taxAmount = taxAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    protected void setTotalAmount( float totalAmount ) {
        this.totalAmount = totalAmount;
    }

    // *******************************
    // Methods
    // *******************************
    public void addQuantity( int quantity ) {
        this.setQuantity( this.quantity + quantity );
    };

    public void resetEconomicalData() {
        this.discountId = null;
        this.taxId = null;
        this.price = 0;
        this.baseAmount = 0;
        this.discountAmount = 0;
        this.taxAmount = 0;
        this.totalAmount = 0;
    }

    public void calculateTotals() throws Exception{
        // TODO: Descuentos e Impuestos según Ids
        this.baseAmount = this.quantity * this.price ;
        this.discountAmount = this.baseAmount * (0.10F);
        this.taxAmount = (this.baseAmount - this.discountAmount) * (0.21F);
        this.totalAmount = this.baseAmount - this.discountAmount + this.taxAmount;
    }

    @Override
    public String toString() {
        return "\t{" +
                //" hashCode: " + this.hashCode() +
                " categoryId: " + this.categoryId +
                "\tproductId: " + this.productId +
                "\tprice: " + this.price +
                "\tquantity: " + this.quantity +
                "\tbase: " + this.baseAmount +
                "\tdiscount: " + this.discountAmount +
                "\ttax: " + this.taxAmount +
                "\ttotalAmount: " + this.totalAmount +
                " }";
    }

    // ********************************
    //         Template  Method
    //         *Design Pattern*
    // ********************************
    public abstract int summarizeQuantity();
    public abstract float summarizeBaseAmount();
    public abstract float summarizeDiscountAmount();
    public abstract float summarizeTaxAmount();
    public abstract float summarizeTotalAmount();

}
