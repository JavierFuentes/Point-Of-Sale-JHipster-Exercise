package asv.exercise.poslogic.shoppingcart.details.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javier on 09/01/16.
 */
public class CompositeItem extends AbstractItem {

    // ********************************
    //            Composite
    //         *Design Pattern*
    // ********************************
    private List< AbstractItem > itemList = new ArrayList<>();

    public final List< AbstractItem > getComponents() {
        return itemList;
    }

    public final void setComponents( List< AbstractItem > itemList ) {
        this.itemList = itemList;
    }

    // *******************************
    // Methods
    // *******************************
    public boolean addItem( AbstractItem item ) throws Exception {
        boolean result;

        // Reset amounts for components (my own Business Rule)
        item.resetEconomicalData();

        result = this.itemList.add( item);

        return result;
    }

    public boolean removeItem( AbstractItem item ) throws Exception {
        boolean result;

        result = this.itemList.remove( item );

        return result;
    }

    // ********************************
    //         Template  Method
    //         *Design Pattern*
    // ********************************
    @Override
    public int summarizeQuantity() {

        // Count every component item as individual (my own Business Rule)
        int sum = 0;

        for ( AbstractItem element : this.getComponents() ) {
            sum += element.summarizeQuantity();
        }

        return sum;

    }

    @Override
    public float summarizeBaseAmount() {
        return getBaseAmount();
    }

    @Override
    public float summarizeDiscountAmount() {
        return getDiscountAmount();
    }

    @Override
    public float summarizeTaxAmount() {
        return getTaxAmount();
    }

    @Override
    public float summarizeTotalAmount() {
        return getTotalAmount();
    }

    @Override
    public String toString() {

        String data = "\n";

        data += "\t{" +
                //" hashCode: " + this.hashCode() +
                " " + this.getClass().getSimpleName().toUpperCase() + ": " +
                //" hashCode: " + this.hashCode() +
                " categoryId: " + this.getCategoryId() +
                "\t\t\t\tproductId: " + this.getProductId() +
                "\t\t\t\t\t\t\tprice: " + this.getPrice() +
                "\t\tquantity: " + this.getQuantity() +
                "\t\tbase: " + this.getBaseAmount() +
                "\t\tdiscount: " + this.getDiscountAmount() +
                "\t\ttax: " + this.getTaxAmount() +
                "\t\ttotalAmount: " + this.getTotalAmount() +
                " }";

        for ( AbstractItem element : this.itemList ) {

            data += "\n*" + element.toString();

//            data += "\n\t\t{" +
//                    //" hashCode: " + element.hashCode() +
//                    "\t*categoryId: " + element.getCategoryId() +
//                    "\t\tproductId: " + element.getProductId() +
//                    "\t\tprice: " + element.getPrice() +
//                    "\t\tquantity: " + element.getQuantity() +
//                    "\t\tbase: " + element.getBaseAmount() +
//                    "\t\tdiscount: " + element.getDiscountAmount() +
//                    "\t\ttax: " + element.getTaxAmount() +
//                    "\t\ttotalAmount: " + element.getTotalAmount() +
//                    " }";

        }

        return data;

    }
}
