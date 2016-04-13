package asv.exercise.web.rest;

import com.codahale.metrics.annotation.Timed;
import asv.exercise.domain.TaxSummary;
import asv.exercise.repository.TaxSummaryRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TaxSummary.
 */
@RestController
@RequestMapping("/api")
public class TaxSummaryResource {

    private final Logger log = LoggerFactory.getLogger(TaxSummaryResource.class);
        
    @Inject
    private TaxSummaryRepository taxSummaryRepository;
    
    /**
     * POST  /taxSummarys -> Create a new taxSummary.
     */
    @RequestMapping(value = "/taxSummarys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaxSummary> createTaxSummary(@RequestBody TaxSummary taxSummary) throws URISyntaxException {
        log.debug("REST request to save TaxSummary : {}", taxSummary);
        if (taxSummary.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("taxSummary", "idexists", "A new taxSummary cannot already have an ID")).body(null);
        }
        TaxSummary result = taxSummaryRepository.save(taxSummary);
        return ResponseEntity.created(new URI("/api/taxSummarys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("taxSummary", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taxSummarys -> Updates an existing taxSummary.
     */
    @RequestMapping(value = "/taxSummarys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaxSummary> updateTaxSummary(@RequestBody TaxSummary taxSummary) throws URISyntaxException {
        log.debug("REST request to update TaxSummary : {}", taxSummary);
        if (taxSummary.getId() == null) {
            return createTaxSummary(taxSummary);
        }
        TaxSummary result = taxSummaryRepository.save(taxSummary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("taxSummary", taxSummary.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taxSummarys -> get all the taxSummarys.
     */
    @RequestMapping(value = "/taxSummarys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TaxSummary>> getAllTaxSummarys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TaxSummarys");
        Page<TaxSummary> page = taxSummaryRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/taxSummarys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /taxSummarys/:id -> get the "id" taxSummary.
     */
    @RequestMapping(value = "/taxSummarys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaxSummary> getTaxSummary(@PathVariable Long id) {
        log.debug("REST request to get TaxSummary : {}", id);
        TaxSummary taxSummary = taxSummaryRepository.findOne(id);
        return Optional.ofNullable(taxSummary)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /taxSummarys/:id -> delete the "id" taxSummary.
     */
    @RequestMapping(value = "/taxSummarys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTaxSummary(@PathVariable Long id) {
        log.debug("REST request to delete TaxSummary : {}", id);
        taxSummaryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("taxSummary", id.toString())).build();
    }
}
