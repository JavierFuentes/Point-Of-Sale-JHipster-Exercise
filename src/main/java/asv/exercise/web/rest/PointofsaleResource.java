package asv.exercise.web.rest;

import com.codahale.metrics.annotation.Timed;
import asv.exercise.domain.Pointofsale;
import asv.exercise.repository.PointofsaleRepository;
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
 * REST controller for managing Pointofsale.
 */
@RestController
@RequestMapping("/api")
public class PointofsaleResource {

    private final Logger log = LoggerFactory.getLogger(PointofsaleResource.class);

    @Inject
    private PointofsaleRepository pointofsaleRepository;

    /**
     * POST  /pointofsales -> Create a new pointofsale.
     */
    @RequestMapping(value = "/pointofsales",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pointofsale> createPointofsale(@RequestBody Pointofsale pointofsale) throws URISyntaxException {
        log.debug("REST request to save Pointofsale : {}", pointofsale);
        if (pointofsale.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pointofsale", "idexists", "A new pointofsale cannot already have an ID")).body(null);
        }
        Pointofsale result = pointofsaleRepository.save(pointofsale);
        return ResponseEntity.created(new URI("/api/pointofsales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pointofsale", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pointofsales -> Updates an existing pointofsale.
     */
    @RequestMapping(value = "/pointofsales",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pointofsale> updatePointofsale(@RequestBody Pointofsale pointofsale) throws URISyntaxException {
        log.debug("REST request to update Pointofsale : {}", pointofsale);
        if (pointofsale.getId() == null) {
            return createPointofsale(pointofsale);
        }
        Pointofsale result = pointofsaleRepository.save(pointofsale);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pointofsale", pointofsale.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pointofsales -> get all the pointofsales.
     */
    @RequestMapping(value = "/pointofsales",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Pointofsale>> getAllPointofsales(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Pointofsales");
        Page<Pointofsale> page = pointofsaleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pointofsales");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pointofsales/:id -> get the "id" pointofsale.
     */
    @RequestMapping(value = "/pointofsales/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Pointofsale> getPointofsale(@PathVariable Long id) {
        log.debug("REST request to get Pointofsale : {}", id);
        Pointofsale pointofsale = pointofsaleRepository.findOne(id);
        return Optional.ofNullable(pointofsale)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pointofsales/:id -> delete the "id" pointofsale.
     */
    @RequestMapping(value = "/pointofsales/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePointofsale(@PathVariable Long id) {
        log.debug("REST request to delete Pointofsale : {}", id);
        pointofsaleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pointofsale", id.toString())).build();
    }

    /**
     * GET  /activePointofsales -> get all the active pointofsales.
     */
    @RequestMapping(value = "/activePointofsales",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Pointofsale>> getAllActivePointofsales(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Active Pointofsales");
        Page<Pointofsale> page = pointofsaleRepository.findAllByActivatedIsTrue(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activePointofsales");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
