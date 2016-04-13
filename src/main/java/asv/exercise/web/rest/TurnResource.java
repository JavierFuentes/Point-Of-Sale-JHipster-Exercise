package asv.exercise.web.rest;

import com.codahale.metrics.annotation.Timed;
import asv.exercise.domain.Turn;
import asv.exercise.repository.TurnRepository;
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
 * REST controller for managing Turn.
 */
@RestController
@RequestMapping("/api")
public class TurnResource {

    private final Logger log = LoggerFactory.getLogger(TurnResource.class);
        
    @Inject
    private TurnRepository turnRepository;
    
    /**
     * POST  /turns -> Create a new turn.
     */
    @RequestMapping(value = "/turns",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Turn> createTurn(@RequestBody Turn turn) throws URISyntaxException {
        log.debug("REST request to save Turn : {}", turn);
        if (turn.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("turn", "idexists", "A new turn cannot already have an ID")).body(null);
        }
        Turn result = turnRepository.save(turn);
        return ResponseEntity.created(new URI("/api/turns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("turn", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /turns -> Updates an existing turn.
     */
    @RequestMapping(value = "/turns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Turn> updateTurn(@RequestBody Turn turn) throws URISyntaxException {
        log.debug("REST request to update Turn : {}", turn);
        if (turn.getId() == null) {
            return createTurn(turn);
        }
        Turn result = turnRepository.save(turn);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("turn", turn.getId().toString()))
            .body(result);
    }

    /**
     * GET  /turns -> get all the turns.
     */
    @RequestMapping(value = "/turns",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Turn>> getAllTurns(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Turns");
        Page<Turn> page = turnRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/turns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /turns/:id -> get the "id" turn.
     */
    @RequestMapping(value = "/turns/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Turn> getTurn(@PathVariable Long id) {
        log.debug("REST request to get Turn : {}", id);
        Turn turn = turnRepository.findOne(id);
        return Optional.ofNullable(turn)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /turns/:id -> delete the "id" turn.
     */
    @RequestMapping(value = "/turns/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTurn(@PathVariable Long id) {
        log.debug("REST request to delete Turn : {}", id);
        turnRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("turn", id.toString())).build();
    }
}
