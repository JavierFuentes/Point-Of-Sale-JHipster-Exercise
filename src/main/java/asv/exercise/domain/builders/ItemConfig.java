package asv.exercise.domain.builders;

import asv.exercise.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by javier on 07/02/16.
 */
public class ItemConfig {

    private final Logger log = LoggerFactory.getLogger( ItemConfig.class );

    private Item item = null;

    public ItemConfig() {
        this.item = new Item();
    }

    public ItemConfig setSale( Sale sale ) {
        item.setSale( sale );
        return this;
    }

    public ItemConfig setCatalogAndQuantity( Catalog catalog, Long quantity ) {
        try {

            item.setQuantity( quantity );

            item.setCatalog( catalog );
            item.setBarcode( catalog.getBarcode() );
            item.setDescription( catalog.getProduct().getDescription() );
            item.setPrice( catalog.getPrice() );
            item.setDiscount( catalog.getProduct().getDiscount() );

            if ( catalog.getProduct().getDiscount() != null ) {
                item.setDiscountpct( catalog.getProduct().getDiscount().getPercentage() );
            }
            else {
                item.setDiscountpct( 0F );
            }

            item.setTax( catalog.getProduct().getTax() );
            item.setTaxrate( catalog.getProduct().getTax().getRate() );

            calculateLine();

        } catch ( Exception e ) {
            log.error( e.toString() );
        }

        return this;
    }

    public Item getItem() {
        return item;
    }

    private ItemConfig calculateLine() {
        try {

            BigDecimal base = item.getPrice().multiply( new BigDecimal( item.getQuantity() ) );

            // Change Discount sign!
            BigDecimal discountamt = base.multiply( new BigDecimal( -item.getDiscountpct() / 100 ) );
            item.setDiscountamount( discountamt );

            base = base.add( discountamt );
            item.setBaseamount( base );


            item.setTaxamount( base.multiply( new BigDecimal( item.getTaxrate() / 100 ) ) );

            item.setTotalamount(
                    item.getBaseamount()
                            .add( item.getTaxamount() )
            );

        } catch ( Exception e ) {
            log.error( e.toString() );
        }

        return this;
    }
}
