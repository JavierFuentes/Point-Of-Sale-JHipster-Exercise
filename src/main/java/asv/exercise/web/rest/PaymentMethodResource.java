package asv.exercise.web.rest;

import com.codahale.metrics.annotation.Timed;
import asv.exercise.domain.PaymentMethod;
import asv.exercise.repository.PaymentMethodRepository;
import asv.exercise.web.rest.util.HeaderUtil;
import asv.exercise.web.rest.util.PaginationUtil;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PaymentMethod.
 */
@RestController
@RequestMapping("/api")
public class PaymentMethodResource {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodResource.class);
        
    @Inject
    private PaymentMethodRepository paymentMethodRepository;
    
    /**
     * POST  /paymentMethods -> Create a new paymentMethod.
     */
    @RequestMapping(value = "/paymentMethods",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaymentMethod> createPaymentMethod(@Valid @RequestBody PaymentMethod paymentMethod) throws URISyntaxException {
        log.debug("REST request to save PaymentMethod : {}", paymentMethod);
        if (paymentMethod.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("paymentMethod", "idexists", "A new paymentMethod cannot already have an ID")).body(null);
        }
        PaymentMethod result = paymentMethodRepository.save(paymentMethod);
        return ResponseEntity.created(new URI("/api/paymentMethods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("paymentMethod", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /paymentMethods -> Updates an existing paymentMethod.
     */
    @RequestMapping(value = "/paymentMethods",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaymentMethod> updatePaymentMethod(@Valid @RequestBody PaymentMethod paymentMethod) throws URISyntaxException {
        log.debug("REST request to update PaymentMethod : {}", paymentMethod);
        if (paymentMethod.getId() == null) {
            return createPaymentMethod(paymentMethod);
        }
        PaymentMethod result = paymentMethodRepository.save(paymentMethod);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("paymentMethod", paymentMethod.getId().toString()))
            .body(result);
    }

    /**
     * GET  /paymentMethods -> get all the paymentMethods.
     */
    @RequestMapping(value = "/paymentMethods",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PaymentMethods");
        Page<PaymentMethod> page = paymentMethodRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/paymentMethods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /paymentMethods/:id -> get the "id" paymentMethod.
     */
    @RequestMapping(value = "/paymentMethods/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaymentMethod> getPaymentMethod(@PathVariable Long id) {
        log.debug("REST request to get PaymentMethod : {}", id);
        PaymentMethod paymentMethod = paymentMethodRepository.findOne(id);
        return Optional.ofNullable(paymentMethod)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /paymentMethods/:id -> delete the "id" paymentMethod.
     */
    @RequestMapping(value = "/paymentMethods/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        log.debug("REST request to delete PaymentMethod : {}", id);
        paymentMethodRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("paymentMethod", id.toString())).build();
    }
}
