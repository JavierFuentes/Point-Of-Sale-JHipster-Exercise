package asv.exercise.web.rest;

import asv.exercise.Application;
import asv.exercise.domain.Pointofsale;
import asv.exercise.repository.PointofsaleRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PointofsaleResource REST controller.
 *
 * @see PointofsaleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PointofsaleResourceIntTest {


    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    @Inject
    private PointofsaleRepository pointofsaleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPointofsaleMockMvc;

    private Pointofsale pointofsale;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PointofsaleResource pointofsaleResource = new PointofsaleResource();
        ReflectionTestUtils.setField(pointofsaleResource, "pointofsaleRepository", pointofsaleRepository);
        this.restPointofsaleMockMvc = MockMvcBuilders.standaloneSetup(pointofsaleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pointofsale = new Pointofsale();
        pointofsale.setActivated(DEFAULT_ACTIVATED);
    }

    @Test
    @Transactional
    public void createPointofsale() throws Exception {
        int databaseSizeBeforeCreate = pointofsaleRepository.findAll().size();

        // Create the Pointofsale

        restPointofsaleMockMvc.perform(post("/api/pointofsales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pointofsale)))
                .andExpect(status().isCreated());

        // Validate the Pointofsale in the database
        List<Pointofsale> pointofsales = pointofsaleRepository.findAll();
        assertThat(pointofsales).hasSize(databaseSizeBeforeCreate + 1);
        Pointofsale testPointofsale = pointofsales.get(pointofsales.size() - 1);
        assertThat(testPointofsale.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllPointofsales() throws Exception {
        // Initialize the database
        pointofsaleRepository.saveAndFlush(pointofsale);

        // Get all the pointofsales
        restPointofsaleMockMvc.perform(get("/api/pointofsales?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pointofsale.getId().intValue())))
                .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));
    }

    @Test
    @Transactional
    public void getPointofsale() throws Exception {
        // Initialize the database
        pointofsaleRepository.saveAndFlush(pointofsale);

        // Get the pointofsale
        restPointofsaleMockMvc.perform(get("/api/pointofsales/{id}", pointofsale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pointofsale.getId().intValue()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPointofsale() throws Exception {
        // Get the pointofsale
        restPointofsaleMockMvc.perform(get("/api/pointofsales/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePointofsale() throws Exception {
        // Initialize the database
        pointofsaleRepository.saveAndFlush(pointofsale);

		int databaseSizeBeforeUpdate = pointofsaleRepository.findAll().size();

        // Update the pointofsale
        pointofsale.setActivated(UPDATED_ACTIVATED);

        restPointofsaleMockMvc.perform(put("/api/pointofsales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pointofsale)))
                .andExpect(status().isOk());

        // Validate the Pointofsale in the database
        List<Pointofsale> pointofsales = pointofsaleRepository.findAll();
        assertThat(pointofsales).hasSize(databaseSizeBeforeUpdate);
        Pointofsale testPointofsale = pointofsales.get(pointofsales.size() - 1);
        assertThat(testPointofsale.getActivated()).isEqualTo(UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void deletePointofsale() throws Exception {
        // Initialize the database
        pointofsaleRepository.saveAndFlush(pointofsale);

		int databaseSizeBeforeDelete = pointofsaleRepository.findAll().size();

        // Get the pointofsale
        restPointofsaleMockMvc.perform(delete("/api/pointofsales/{id}", pointofsale.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pointofsale> pointofsales = pointofsaleRepository.findAll();
        assertThat(pointofsales).hasSize(databaseSizeBeforeDelete - 1);
    }
}
