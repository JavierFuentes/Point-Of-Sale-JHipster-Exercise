package asv.exercise.service.util;

import asv.exercise.domain.Product;
import asv.exercise.repository.DiscountRepository;
import asv.exercise.repository.ProductRepository;
import asv.exercise.repository.TaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.unbescape.html.HtmlEscape;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by javier on 07/02/16.
 */
@Service
@Transactional
public class UpcDatabaseConnectorService {

    private final Logger log = LoggerFactory.getLogger( UpcDatabaseConnectorService.class );

    @Inject
    private TaxRepository taxRepository;

    @Inject
    private DiscountRepository discountRepository;

    @Inject
    private ProductRepository productRepository;

    @Async
    private Future< UpcDatabase > findBarcode( String barcode ) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        UpcDatabase results = restTemplate.getForObject( "http://api.upcdatabase.org/json/2316e70001a2757dc55dc64ce0465787/" + barcode, UpcDatabase.class );

        return new AsyncResult<>( results );
    }

    /**
     * Search in online API and saves in DB a new Product with random Price and Discount
     * This API returns result with a partial barcode, so we only call the API with 13 digits barcode
     * <p>
     * To get a random Barcode: http://upcdatabase.org/random
     * API Docs: http://upcdatabase.org/api
     * Example GET call: http://api.upcdatabase.org/json/2316e70001a2757dc55dc64ce0465787/4060800002242
     **/
    public Optional< Product > searchAPIAndCreateNewProduct( String barcode ) throws InterruptedException, ExecutionException, TimeoutException {

        Optional< Product > newProduct = Optional.empty();

        if ( barcode.length() != 13 ) {
            return newProduct;
        }

        // Asynchronous call
        Future< UpcDatabase > futureUPC = findBarcode( barcode );
//        while ( !futureUPC.isDone() ) {
//            log.info( "Waiting for upcdatabase.org..." );
//            TimeUnit.MILLISECONDS.sleep( 10 );
//        }
        UpcDatabase upc = futureUPC.get(1000, TimeUnit.MILLISECONDS);

        log.debug( upc.toString() );

        if (upc.getValid().equals( "false" )) {
            return newProduct;
        }

        String description = null;

        if ( !upc.getAlias().equals( "" ) )
            description = upc.getAlias();

        if ( !upc.getDescription().equals( "" ) ) {
            description = upc.getDescription();
        }

        if ( !upc.getItemname().equals( "" ) ) {
            description = upc.getItemname();
        }

        if ( description != null ) {
            // Randomize Tax and Discount
            Double randomTaxId = 1 + ( Math.random() * ( taxRepository.count() ) );
            Double randomDiscountId = ( Math.random() * ( discountRepository.count() + 1 ) );

            // MaxLength = 20
            description = description.trim();
            description = description.substring( 0, Math.min( 19, description.length() ) );
            description = HtmlEscape.unescapeHtml( description );

            // Insert a new record in Product table
            newProduct = Optional.of( new Product() );
            newProduct.get().setDescription( description );
            newProduct.get().setTax( taxRepository.getOne( randomTaxId.longValue() ) );

            if ( randomDiscountId.longValue() != 0 )
                newProduct.get().setDiscount( discountRepository.getOne( randomDiscountId.longValue() ) );

            newProduct = Optional.ofNullable( productRepository.save( newProduct.get() ) );
        }

        return newProduct;
    }
}
