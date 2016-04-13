package asv.exercise.web.rest;

import asv.exercise.Application;
import asv.exercise.domain.Item;
import asv.exercise.repository.ItemRepository;
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
 * Test class for the ItemResource REST controller.
 *
 * @see ItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ItemResourceIntTest {

    private static final String DEFAULT_BARCODE = "AAAAA";
    private static final String UPDATED_BARCODE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Float DEFAULT_DISCOUNTPCT = 1F;
    private static final Float UPDATED_DISCOUNTPCT = 2F;

    private static final BigDecimal DEFAULT_DISCOUNTAMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNTAMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BASEAMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_BASEAMOUNT = new BigDecimal(2);

    private static final Float DEFAULT_TAXRATE = 1F;
    private static final Float UPDATED_TAXRATE = 2F;

    private static final BigDecimal DEFAULT_TAXAMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXAMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTALAMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTALAMOUNT = new BigDecimal(2);

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restItemMockMvc;

    private Item item;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemResource itemResource = new ItemResource();
        ReflectionTestUtils.setField(itemResource, "itemRepository", itemRepository);
        this.restItemMockMvc = MockMvcBuilders.standaloneSetup(itemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        item = new Item();
        item.setBarcode(DEFAULT_BARCODE);
        item.setDescription(DEFAULT_DESCRIPTION);
        item.setQuantity(DEFAULT_QUANTITY);
        item.setPrice(DEFAULT_PRICE);
        item.setDiscountpct(DEFAULT_DISCOUNTPCT);
        item.setDiscountamount(DEFAULT_DISCOUNTAMOUNT);
        item.setBaseamount(DEFAULT_BASEAMOUNT);
        item.setTaxrate(DEFAULT_TAXRATE);
        item.setTaxamount(DEFAULT_TAXAMOUNT);
        item.setTotalamount(DEFAULT_TOTALAMOUNT);
    }

    @Test
    @Transactional
    public void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // Create the Item

        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = items.get(items.size() - 1);
        assertThat(testItem.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItem.getQuantity().intValue()).isEqualTo(DEFAULT_QUANTITY.intValue());
        assertThat(testItem.getPrice().doubleValue()).isEqualTo(DEFAULT_PRICE.doubleValue());
        assertThat(testItem.getDiscountpct().doubleValue()).isEqualTo(DEFAULT_DISCOUNTPCT.doubleValue());
        assertThat(testItem.getDiscountamount().doubleValue()).isEqualTo(DEFAULT_DISCOUNTAMOUNT.doubleValue());
        assertThat(testItem.getBaseamount().doubleValue()).isEqualTo(DEFAULT_BASEAMOUNT.doubleValue());
        assertThat(testItem.getTaxrate().doubleValue()).isEqualTo(DEFAULT_TAXRATE.doubleValue());
        assertThat(testItem.getTaxamount().doubleValue()).isEqualTo(DEFAULT_TAXAMOUNT.doubleValue());
        assertThat(testItem.getTotalamount().doubleValue()).isEqualTo(DEFAULT_TOTALAMOUNT.doubleValue());
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the items
        restItemMockMvc.perform(get("/api/items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
                .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].discountpct").value(hasItem(DEFAULT_DISCOUNTPCT.doubleValue())))
                .andExpect(jsonPath("$.[*].discountamount").value(hasItem(DEFAULT_DISCOUNTAMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].baseamount").value(hasItem(DEFAULT_BASEAMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].taxrate").value(hasItem(DEFAULT_TAXRATE.doubleValue())))
                .andExpect(jsonPath("$.[*].taxamount").value(hasItem(DEFAULT_TAXAMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].totalamount").value(hasItem(DEFAULT_TOTALAMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.discountpct").value(DEFAULT_DISCOUNTPCT.doubleValue()))
            .andExpect(jsonPath("$.discountamount").value(DEFAULT_DISCOUNTAMOUNT.doubleValue()))
            .andExpect(jsonPath("$.baseamount").value(DEFAULT_BASEAMOUNT.doubleValue()))
            .andExpect(jsonPath("$.taxrate").value(DEFAULT_TAXRATE.doubleValue()))
            .andExpect(jsonPath("$.taxamount").value(DEFAULT_TAXAMOUNT.doubleValue()))
            .andExpect(jsonPath("$.totalamount").value(DEFAULT_TOTALAMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

		int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        item.setBarcode(UPDATED_BARCODE);
        item.setDescription(UPDATED_DESCRIPTION);
        item.setQuantity(UPDATED_QUANTITY);
        item.setPrice(UPDATED_PRICE);
        item.setDiscountpct(UPDATED_DISCOUNTPCT);
        item.setDiscountamount(UPDATED_DISCOUNTAMOUNT);
        item.setBaseamount(UPDATED_BASEAMOUNT);
        item.setTaxrate(UPDATED_TAXRATE);
        item.setTaxamount(UPDATED_TAXAMOUNT);
        item.setTotalamount(UPDATED_TOTALAMOUNT);

        restItemMockMvc.perform(put("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeUpdate);
        Item testItem = items.get(items.size() - 1);
        assertThat(testItem.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItem.getQuantity().intValue()).isEqualTo(UPDATED_QUANTITY.intValue());
        assertThat(testItem.getPrice().intValue()).isEqualTo(UPDATED_PRICE.intValue());
        assertThat(testItem.getDiscountpct().intValue()).isEqualTo(UPDATED_DISCOUNTPCT.intValue());
        assertThat(testItem.getDiscountamount().intValue()).isEqualTo(UPDATED_DISCOUNTAMOUNT.intValue());
        assertThat(testItem.getBaseamount().intValue()).isEqualTo(UPDATED_BASEAMOUNT.intValue());
        assertThat(testItem.getTaxrate().intValue()).isEqualTo(UPDATED_TAXRATE.intValue());
        assertThat(testItem.getTaxamount().intValue()).isEqualTo(UPDATED_TAXAMOUNT.intValue());
        assertThat(testItem.getTotalamount().intValue()).isEqualTo(UPDATED_TOTALAMOUNT.intValue());
    }

    @Test
    @Transactional
    public void deleteItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

		int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Get the item
        restItemMockMvc.perform(delete("/api/items/{id}", item.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeDelete - 1);
    }
}
