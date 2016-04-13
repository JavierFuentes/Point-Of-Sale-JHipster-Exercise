package asv.exercise.web.rest;

import com.codahale.metrics.annotation.Timed;
import asv.exercise.domain.Shop;
import asv.exercise.repository.ShopRepository;
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
 * REST controller for managing Shop.
 */
@RestController
@RequestMapping("/api")
public class ShopResource {

    private final Logger log = LoggerFactory.getLogger(ShopResource.class);
        
    @Inject
    private ShopRepository shopRepository;
    
    /**
     * POST  /shops -> Create a new shop.
     */
    @RequestMapping(value = "/shops",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shop> createShop(@Valid @RequestBody Shop shop) throws URISyntaxException {
        log.debug("REST request to save Shop : {}", shop);
        if (shop.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shop", "idexists", "A new shop cannot already have an ID")).body(null);
        }
        Shop result = shopRepository.save(shop);
        return ResponseEntity.created(new URI("/api/shops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shop", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shops -> Updates an existing shop.
     */
    @RequestMapping(value = "/shops",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shop> updateShop(@Valid @RequestBody Shop shop) throws URISyntaxException {
        log.debug("REST request to update Shop : {}", shop);
        if (shop.getId() == null) {
            return createShop(shop);
        }
        Shop result = shopRepository.save(shop);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shop", shop.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shops -> get all the shops.
     */
    @RequestMapping(value = "/shops",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Shop>> getAllShops(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Shops");
        Page<Shop> page = shopRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shops");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /shops/:id -> get the "id" shop.
     */
    @RequestMapping(value = "/shops/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Shop> getShop(@PathVariable Long id) {
        log.debug("REST request to get Shop : {}", id);
        Shop shop = shopRepository.findOne(id);
        return Optional.ofNullable(shop)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shops/:id -> delete the "id" shop.
     */
    @RequestMapping(value = "/shops/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        log.debug("REST request to delete Shop : {}", id);
        shopRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shop", id.toString())).build();
    }
}
