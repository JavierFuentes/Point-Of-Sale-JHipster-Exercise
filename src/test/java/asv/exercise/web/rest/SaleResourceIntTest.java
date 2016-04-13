package asv.exercise.web.rest;

import asv.exercise.Application;
import asv.exercise.domain.Sale;
import asv.exercise.repository.SaleRepository;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SaleResource REST controller.
 *
 * @see SaleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SaleResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_STR = dateTimeFormatter.format(DEFAULT_TIME);

    private static final BigDecimal DEFAULT_TOTALAMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTALAMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PAYEDAMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYEDAMOUNT = new BigDecimal(2);

    @Inject
    private SaleRepository saleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSaleMockMvc;

    private Sale sale;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SaleResource saleResource = new SaleResource();
        ReflectionTestUtils.setField(saleResource, "saleRepository", saleRepository);
        this.restSaleMockMvc = MockMvcBuilders.standaloneSetup(saleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sale = new Sale();
        sale.setCompleted(DEFAULT_COMPLETED);
        sale.setTime(DEFAULT_TIME);
        sale.setTotalamount(DEFAULT_TOTALAMOUNT);
        sale.setPayedamount(DEFAULT_PAYEDAMOUNT);
    }

    @Test
    @Transactional
    public void createSale() throws Exception {
        int databaseSizeBeforeCreate = saleRepository.findAll().size();

        // Create the Sale

        restSaleMockMvc.perform(post("/api/sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sale)))
                .andExpect(status().isCreated());

        // Validate the Sale in the database
        List<Sale> sales = saleRepository.findAll();
        assertThat(sales).hasSize(databaseSizeBeforeCreate + 1);
        Sale testSale = sales.get(sales.size() - 1);
        assertThat(testSale.getCompleted()).isEqualTo(DEFAULT_COMPLETED);
        assertThat(testSale.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testSale.getTotalamount().intValue()).isEqualTo(DEFAULT_TOTALAMOUNT.intValue());
        assertThat(testSale.getPayedamount().intValue()).isEqualTo(DEFAULT_PAYEDAMOUNT.intValue());
    }

    @Test
    @Transactional
    public void getAllSales() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

        // Get all the sales
        restSaleMockMvc.perform(get("/api/sales?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sale.getId().intValue())))
                .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)))
                .andExpect(jsonPath("$.[*].totalamount").value(hasItem(DEFAULT_TOTALAMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].payedamount").value(hasItem(DEFAULT_PAYEDAMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getSale() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

        // Get the sale
        restSaleMockMvc.perform(get("/api/sales/{id}", sale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sale.getId().intValue()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR))
            .andExpect(jsonPath("$.totalamount").value(DEFAULT_TOTALAMOUNT.doubleValue()))
            .andExpect(jsonPath("$.payedamount").value(DEFAULT_PAYEDAMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSale() throws Exception {
        // Get the sale
        restSaleMockMvc.perform(get("/api/sales/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSale() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

		int databaseSizeBeforeUpdate = saleRepository.findAll().size();

        // Update the sale
        sale.setCompleted(UPDATED_COMPLETED);
        sale.setTime(UPDATED_TIME);
        sale.setTotalamount(UPDATED_TOTALAMOUNT);
        sale.setPayedamount(UPDATED_PAYEDAMOUNT);

        restSaleMockMvc.perform(put("/api/sales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sale)))
                .andExpect(status().isOk());

        // Validate the Sale in the database
        List<Sale> sales = saleRepository.findAll();
        assertThat(sales).hasSize(databaseSizeBeforeUpdate);
        Sale testSale = sales.get(sales.size() - 1);
        assertThat(testSale.getCompleted()).isEqualTo(UPDATED_COMPLETED);
        assertThat(testSale.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testSale.getTotalamount().intValue()).isEqualTo(UPDATED_TOTALAMOUNT.intValue());
        assertThat(testSale.getPayedamount().intValue()).isEqualTo(UPDATED_PAYEDAMOUNT.intValue());
    }

    @Test
    @Transactional
    public void deleteSale() throws Exception {
        // Initialize the database
        saleRepository.saveAndFlush(sale);

		int databaseSizeBeforeDelete = saleRepository.findAll().size();

        // Get the sale
        restSaleMockMvc.perform(delete("/api/sales/{id}", sale.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sale> sales = saleRepository.findAll();
        assertThat(sales).hasSize(databaseSizeBeforeDelete - 1);
    }
}
