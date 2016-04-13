package asv.exercise.web.rest;

import asv.exercise.Application;
import asv.exercise.domain.Catalog;
import asv.exercise.repository.CatalogRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CatalogResource REST controller.
 *
 * @see CatalogResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CatalogResourceIntTest {

    private static final String DEFAULT_BARCODE = "AAAAA";
    private static final String UPDATED_BARCODE = "BBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Long DEFAULT_INVENTORY = 1L;
    private static final Long UPDATED_INVENTORY = 2L;

    @Inject
    private CatalogRepository catalogRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCatalogMockMvc;

    private Catalog catalog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CatalogResource catalogResource = new CatalogResource();
        ReflectionTestUtils.setField(catalogResource, "catalogRepository", catalogRepository);
        this.restCatalogMockMvc = MockMvcBuilders.standaloneSetup(catalogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        catalog = new Catalog();
        catalog.setBarcode(DEFAULT_BARCODE);
        catalog.setPrice(DEFAULT_PRICE);
        catalog.setInventory(DEFAULT_INVENTORY);
    }

    @Test
    @Transactional
    public void createCatalog() throws Exception {
        int databaseSizeBeforeCreate = catalogRepository.findAll().size();

        // Create the Catalog

        restCatalogMockMvc.perform(post("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalog)))
                .andExpect(status().isCreated());

        // Validate the Catalog in the database
        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeCreate + 1);
        Catalog testCatalog = catalogs.get(catalogs.size() - 1);
        assertThat(testCatalog.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testCatalog.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCatalog.getInventory()).isEqualTo(DEFAULT_INVENTORY);
    }

    @Test
    @Transactional
    public void checkBarcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogRepository.findAll().size();
        // set the field null
        catalog.setBarcode(null);

        // Create the Catalog, which fails.

        restCatalogMockMvc.perform(post("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalog)))
                .andExpect(status().isBadRequest());

        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogRepository.findAll().size();
        // set the field null
        catalog.setPrice(null);

        // Create the Catalog, which fails.

        restCatalogMockMvc.perform(post("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalog)))
                .andExpect(status().isBadRequest());

        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInventoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogRepository.findAll().size();
        // set the field null
        catalog.setInventory(null);

        // Create the Catalog, which fails.

        restCatalogMockMvc.perform(post("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalog)))
                .andExpect(status().isBadRequest());

        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCatalogs() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        // Get all the catalogs
        restCatalogMockMvc.perform(get("/api/catalogs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(catalog.getId().intValue())))
                .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].inventory").value(hasItem(DEFAULT_INVENTORY.intValue())));
    }

    @Test
    @Transactional
    public void getCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        // Get the catalog
        restCatalogMockMvc.perform(get("/api/catalogs/{id}", catalog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(catalog.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.inventory").value(DEFAULT_INVENTORY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCatalog() throws Exception {
        // Get the catalog
        restCatalogMockMvc.perform(get("/api/catalogs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

		int databaseSizeBeforeUpdate = catalogRepository.findAll().size();

        // Update the catalog
        catalog.setBarcode(UPDATED_BARCODE);
        catalog.setPrice(UPDATED_PRICE);
        catalog.setInventory(UPDATED_INVENTORY);

        restCatalogMockMvc.perform(put("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalog)))
                .andExpect(status().isOk());

        // Validate the Catalog in the database
        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeUpdate);
        Catalog testCatalog = catalogs.get(catalogs.size() - 1);
        assertThat(testCatalog.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testCatalog.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCatalog.getInventory()).isEqualTo(UPDATED_INVENTORY);
    }

    @Test
    @Transactional
    public void deleteCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

		int databaseSizeBeforeDelete = catalogRepository.findAll().size();

        // Get the catalog
        restCatalogMockMvc.perform(delete("/api/catalogs/{id}", catalog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
