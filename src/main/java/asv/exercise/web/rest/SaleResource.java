package asv.exercise.web.rest;

import asv.exercise.domain.Sale;
import asv.exercise.repository.SaleRepository;
import asv.exercise.service.SaleService;
import asv.exercise.web.rest.util.HeaderUtil;
import asv.exercise.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * REST controller for managing Sale.
 */
@RestController
@RequestMapping( "/api" )
public class SaleResource {

    private final Logger log = LoggerFactory.getLogger( SaleResource.class );

    @Inject
    private SaleRepository saleRepository;

    @Inject
    SaleService saleService;

    /**
     * POST  /sales -> Create a new sale.
     */
    @RequestMapping( value = "/sales",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @Timed
    public ResponseEntity< Sale > createSale( @RequestBody Sale sale ) throws URISyntaxException {
        log.debug( "REST request to save Sale : {}", sale );
        if ( sale.getId() != null ) {
            return ResponseEntity.badRequest().headers( HeaderUtil.createFailureAlert( "sale", "idexists", "A new sale cannot already have an ID" ) ).body( null );
        }
        Sale result = saleRepository.save( sale );
        return ResponseEntity.created( new URI( "/api/sales/" + result.getId() ) )
                .headers( HeaderUtil.createEntityCreationAlert( "sale", result.getId().toString() ) )
                .body( result );
    }

    /**
     * PUT  /sales -> Updates an existing sale.
     */
    @RequestMapping( value = "/sales",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @Timed
    public ResponseEntity< Sale > updateSale( @RequestBody Sale sale ) throws URISyntaxException {
        log.debug( "REST request to update Sale : {}", sale );
        if ( sale.getId() == null ) {
            return createSale( sale );
        }
        Sale result = saleRepository.save( sale );
        return ResponseEntity.ok()
                .headers( HeaderUtil.createEntityUpdateAlert( "sale", sale.getId().toString() ) )
                .body( result );
    }

    /**
     * GET  /sales -> get all the sales.
     */
    @RequestMapping( value = "/sales",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @Timed
    public ResponseEntity< List< Sale > > getAllSales( Pageable pageable )
            throws URISyntaxException {
        log.debug( "REST request to get a page of Sales" );
        Page< Sale > page = saleRepository.findAll( pageable );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders( page, "/api/sales" );
        return new ResponseEntity<>( page.getContent(), headers, HttpStatus.OK );
    }

    /**
     * GET  /sales/:id -> get the "id" sale.
     */
    @RequestMapping( value = "/sales/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @Timed
    public ResponseEntity< Sale > getSale( @PathVariable Long id ) throws URISyntaxException {
        log.debug( "REST request to get Sale : {}", id );
        Sale sale = saleRepository.findOne( id );
        return Optional.ofNullable( sale )
                .map( result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK ) )
                .orElse( new ResponseEntity<>( HttpStatus.NOT_FOUND ) );
    }

    /**
     * DELETE  /sales/:id -> delete the "id" sale.
     */
    @RequestMapping( value = "/sales/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @Timed
    public ResponseEntity< Void > deleteSale( @PathVariable Long id ) {
        log.debug( "REST request to delete Sale : {}", id );
        saleRepository.delete( id );
        return ResponseEntity.ok().headers( HeaderUtil.createEntityDeletionAlert( "sale", id.toString() ) ).build();
    }

    /**
     * PUT  /sales/:id/add/:barcode/:qty -> Updates an existing sale adding a new barcode (item)
     */
    @RequestMapping( value = "/sales/{id}/add/{barcode}/{qty}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @Timed
    public ResponseEntity< Sale > addItemToSale( @PathVariable Long id, @PathVariable String barcode, @PathVariable Long qty ) throws URISyntaxException, ExecutionException, InterruptedException, TimeoutException {

        log.debug( "REST request to add Barcode {} to Sale : {}", barcode, id );

        Optional< Sale > updatedSale = saleService.addBarcodeToSale( id, barcode, qty );

        Sale result;
        ResponseEntity< Sale > response;

        if ( updatedSale.isPresent() ) {

            result = updatedSale.get();

            response = ResponseEntity.status( HttpStatus.OK )
                    .headers( HeaderUtil.createEntityUpdateAlert( "sale", result.getId().toString() ) )
                    .body( result );

        }
        else {

            result = saleRepository.findOne( id );

            response = ResponseEntity.status( HttpStatus.NOT_FOUND )
                    .headers( HeaderUtil.createFailureAlert( "sale", result.getId().toString(), "barcode " + barcode + " does not exists in shop catalog!" ) )
                    .body( result );

        }

        return response;

    }

    /**
     * GET  /sales/:id/summary -> persists TaxSummary calculations and returns a json
     * with all needed data to show in client side
     */
    @RequestMapping( value = "/sales/{id}/summary",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @Timed
    public ResponseEntity< Sale > getSummarySale( @PathVariable Long id ) throws URISyntaxException {
        log.debug( "REST request to get Sale Summary : {}", id );

        Optional< Sale > updatedSale = saleService.calculateTaxSummary( id );

        Sale result;
        ResponseEntity< Sale > response;

        if ( updatedSale.isPresent() ) {

            result = updatedSale.get();

            response = ResponseEntity.status( HttpStatus.OK )
                    .body( result );

        } else {

            result = saleRepository.findOne( id );

            response = ResponseEntity.status( HttpStatus.NO_CONTENT )
                    .headers( HeaderUtil.createFailureAlert( "sale", result.getId().toString(), "Can't persist TaxSummary calculations!" ) )
                    .body( result );

        }

        return response;

    }

    /**
     * PUT  /sales/:id/pay/:payedamount/:paymentmethod -> Updates an existing sale with the payment method and payed amount
     */
    @RequestMapping( value = "/sales/{id}/pay/{payedamount}/{paymentmethod}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @Timed
    public ResponseEntity< Sale > paySale( @PathVariable Long id, @PathVariable BigDecimal payedamount, @PathVariable String paymentmethod ) throws URISyntaxException {

        log.debug( "REST request to pay {} € by {} to Sale {}", payedamount, paymentmethod, id );

        Optional< Sale > updatedSale = saleService.paySale( id, payedamount, paymentmethod );

        Sale result;
        ResponseEntity< Sale > response;

        if ( updatedSale.isPresent() ) {

            result = updatedSale.get();

            response = ResponseEntity.status( HttpStatus.OK )
                    .headers( HeaderUtil.createEntityUpdateAlert( "sale", result.getId().toString() ) )
                    .body( result );

        }
        else {

            result = saleRepository.findOne( id );

            response = ResponseEntity.status( HttpStatus.NOT_ACCEPTABLE )
                    .headers( HeaderUtil.createFailureAlert( "sale", result.getId().toString(), " Can't find Payment Method " + paymentmethod ) )
                    .body( result );

        }

        return response;
    }

    /**
     * GET  /lastuncompletedsale
     *
     * Returns, if exists, the last User active Turn uncompleted Sale.
     * If not, creates a new Sale to return attached to User active Turn.
     */
    @RequestMapping( value = "/lastuncompletedsale",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @Timed
    public ResponseEntity< Sale > findLastOrCreateNewUncompletedSale() throws URISyntaxException {
        log.debug( "REST request to get Last Uncompleted Sale of User Active Turn" );

        Optional<Sale> currentSale = saleService.findLastOrCreateNewUncompletedSale();

        return currentSale
                .map( result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK ) )
                .orElse( new ResponseEntity<>( HttpStatus.PRECONDITION_FAILED ) );
    }

}
