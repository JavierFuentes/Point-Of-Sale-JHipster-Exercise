package asv.exercise.web.rest;

import asv.exercise.Application;
import asv.exercise.domain.TaxSummary;
import asv.exercise.repository.TaxSummaryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TaxSummaryResource REST controller.
 *
 * @see TaxSummaryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TaxSummaryResourceIntTest {


    private static final BigDecimal DEFAULT_TAXBASE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXBASE = new BigDecimal(2);

    private static final Float DEFAULT_TAXRATE = 1F;
    private static final Float UPDATED_TAXRATE = 2F;

    private static final BigDecimal DEFAULT_TAXAMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXAMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTALAMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTALAMOUNT = new BigDecimal(2);

    @Inject
    private TaxSummaryRepository taxSummaryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaxSummaryMockMvc;

    private TaxSummary taxSummary;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaxSummaryResource taxSummaryResource = new TaxSummaryResource();
        ReflectionTestUtils.setField(taxSummaryResource, "taxSummaryRepository", taxSummaryRepository);
        this.restTaxSummaryMockMvc = MockMvcBuilders.standaloneSetup(taxSummaryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taxSummary = new TaxSummary();
        taxSummary.setTaxbase(DEFAULT_TAXBASE);
        taxSummary.setTaxrate(DEFAULT_TAXRATE);
        taxSummary.setTaxamount(DEFAULT_TAXAMOUNT);
        taxSummary.setTotalamount(DEFAULT_TOTALAMOUNT);
    }

    @Test
    @Transactional
    public void createTaxSummary() throws Exception {
        int databaseSizeBeforeCreate = taxSummaryRepository.findAll().size();

        // Create the TaxSummary

        restTaxSummaryMockMvc.perform(post("/api/taxSummarys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taxSummary)))
                .andExpect(status().isCreated());

        // Validate the TaxSummary in the database
        List<TaxSummary> taxSummarys = taxSummaryRepository.findAll();
        assertThat(taxSummarys).hasSize(databaseSizeBeforeCreate + 1);
        TaxSummary testTaxSummary = taxSummarys.get(taxSummarys.size() - 1);
        assertThat(testTaxSummary.getTaxbase().intValue()).isEqualTo(DEFAULT_TAXBASE.intValue());
        assertThat(testTaxSummary.getTaxrate().intValue()).isEqualTo(DEFAULT_TAXRATE.intValue());
        assertThat(testTaxSummary.getTaxamount().intValue()).isEqualTo(DEFAULT_TAXAMOUNT.intValue());
        assertThat(testTaxSummary.getTotalamount().intValue()).isEqualTo(DEFAULT_TOTALAMOUNT.intValue());
    }

    @Test
    @Transactional
    public void getAllTaxSummarys() throws Exception {
        // Initialize the database
        taxSummaryRepository.saveAndFlush(taxSummary);

        // Get all the taxSummarys
        restTaxSummaryMockMvc.perform(get("/api/taxSummarys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(taxSummary.getId().intValue())))
                .andExpect(jsonPath("$.[*].taxbase").value(hasItem(DEFAULT_TAXBASE.doubleValue())))
                .andExpect(jsonPath("$.[*].taxrate").value(hasItem(DEFAULT_TAXRATE.doubleValue())))
                .andExpect(jsonPath("$.[*].taxamount").value(hasItem(DEFAULT_TAXAMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].totalamount").value(hasItem(DEFAULT_TOTALAMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getTaxSummary() throws Exception {
        // Initialize the database
        taxSummaryRepository.saveAndFlush(taxSummary);

        // Get the taxSummary
        restTaxSummaryMockMvc.perform(get("/api/taxSummarys/{id}", taxSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(taxSummary.getId().intValue()))
            .andExpect(jsonPath("$.taxbase").value(DEFAULT_TAXBASE.doubleValue()))
            .andExpect(jsonPath("$.taxrate").value(DEFAULT_TAXRATE.doubleValue()))
            .andExpect(jsonPath("$.taxamount").value(DEFAULT_TAXAMOUNT.doubleValue()))
            .andExpect(jsonPath("$.totalamount").value(DEFAULT_TOTALAMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTaxSummary() throws Exception {
        // Get the taxSummary
        restTaxSummaryMockMvc.perform(get("/api/taxSummarys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaxSummary() throws Exception {
        // Initialize the database
        taxSummaryRepository.saveAndFlush(taxSummary);

		int databaseSizeBeforeUpdate = taxSummaryRepository.findAll().size();

        // Update the taxSummary
        taxSummary.setTaxbase(UPDATED_TAXBASE);
        taxSummary.setTaxrate(UPDATED_TAXRATE);
        taxSummary.setTaxamount(UPDATED_TAXAMOUNT);
        taxSummary.setTotalamount(UPDATED_TOTALAMOUNT);

        restTaxSummaryMockMvc.perform(put("/api/taxSummarys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taxSummary)))
                .andExpect(status().isOk());

        // Validate the TaxSummary in the database
        List<TaxSummary> taxSummarys = taxSummaryRepository.findAll();
        assertThat(taxSummarys).hasSize(databaseSizeBeforeUpdate);
        TaxSummary testTaxSummary = taxSummarys.get(taxSummarys.size() - 1);
        assertThat(testTaxSummary.getTaxbase().intValue()).isEqualTo(UPDATED_TAXBASE.intValue());
        assertThat(testTaxSummary.getTaxrate().intValue()).isEqualTo(UPDATED_TAXRATE.intValue());
        assertThat(testTaxSummary.getTaxamount().intValue()).isEqualTo(UPDATED_TAXAMOUNT.intValue());
        assertThat(testTaxSummary.getTotalamount().intValue()).isEqualTo(UPDATED_TOTALAMOUNT.intValue());
    }

    @Test
    @Transactional
    public void deleteTaxSummary() throws Exception {
        // Initialize the database
        taxSummaryRepository.saveAndFlush(taxSummary);

		int databaseSizeBeforeDelete = taxSummaryRepository.findAll().size();

        // Get the taxSummary
        restTaxSummaryMockMvc.perform(delete("/api/taxSummarys/{id}", taxSummary.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TaxSummary> taxSummarys = taxSummaryRepository.findAll();
        assertThat(taxSummarys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
