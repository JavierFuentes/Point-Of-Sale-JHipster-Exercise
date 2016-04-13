package asv.exercise.poslogic.shoppingcart.details.domain;

import asv.exercise.domain.AbstractAuditingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by javier on 10/01/16.
 */
public class ProductCategory extends AbstractAuditingEntity implements Serializable {

    private static final Logger log = LoggerFactory.getLogger( ProductCategory.class );

    // *******************************
    // Fields and Properties
    // *******************************

    private String categoryId = "";
    private String description = "";

    private String discountId = "";
    private String taxId = "";

}
