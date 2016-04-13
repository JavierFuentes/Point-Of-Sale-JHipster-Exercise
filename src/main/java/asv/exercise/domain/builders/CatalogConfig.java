package asv.exercise.domain.builders;

import asv.exercise.domain.Catalog;
import asv.exercise.domain.Item;
import asv.exercise.domain.Product;
import asv.exercise.domain.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by javier on 07/02/16.
 */
public class CatalogConfig {

    private final Logger log = LoggerFactory.getLogger( CatalogConfig.class );

    private Catalog catalog = null;

    public CatalogConfig() {
        this.catalog = new Catalog();
    }

    public CatalogConfig setBarcode( String barcode ) {
        this.catalog.setBarcode( barcode );
        return this;
    }

    public CatalogConfig setPrice(BigDecimal price) {
        this.catalog.setPrice( price );
        return this;
    }

    public CatalogConfig setInventory(Long inventory) {
        this.catalog.setInventory( inventory );
        return this;
    }

    public CatalogConfig setProduct(Product product) {
        this.catalog.setProduct( product );
        return this;
    }

    public CatalogConfig setShop(Shop shop) {
        this.catalog.setShop( shop );
        return this;
    }

    public CatalogConfig setItems(Set<Item> items) {
        this.catalog.setItems( items );
        return this;
    }

    public Catalog getCatalog() {
        return this.catalog;
    }
}
