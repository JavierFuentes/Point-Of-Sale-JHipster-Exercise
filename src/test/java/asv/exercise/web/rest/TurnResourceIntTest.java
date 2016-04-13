package asv.exercise.web.rest;

import asv.exercise.Application;
import asv.exercise.domain.Turn;
import asv.exercise.repository.TurnRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TurnResource REST controller.
 *
 * @see TurnResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TurnResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final ZonedDateTime DEFAULT_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_STR = dateTimeFormatter.format(DEFAULT_START);

    private static final ZonedDateTime DEFAULT_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_STR = dateTimeFormatter.format(DEFAULT_END);

    @Inject
    private TurnRepository turnRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTurnMockMvc;

    private Turn turn;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TurnResource turnResource = new TurnResource();
        ReflectionTestUtils.setField(turnResource, "turnRepository", turnRepository);
        this.restTurnMockMvc = MockMvcBuilders.standaloneSetup(turnResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        turn = new Turn();
        turn.setActivated(DEFAULT_ACTIVATED);
        turn.setStarted(DEFAULT_START);
        turn.setEnded(DEFAULT_END);
    }

    @Test
    @Transactional
    public void createTurn() throws Exception {
        int databaseSizeBeforeCreate = turnRepository.findAll().size();

        // Create the Turn

        restTurnMockMvc.perform(post("/api/turns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(turn)))
                .andExpect(status().isCreated());

        // Validate the Turn in the database
        List<Turn> turns = turnRepository.findAll();
        assertThat(turns).hasSize(databaseSizeBeforeCreate + 1);
        Turn testTurn = turns.get(turns.size() - 1);
        assertThat(testTurn.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testTurn.getStarted()).isEqualTo(DEFAULT_START);
        assertThat(testTurn.getEnded()).isEqualTo(DEFAULT_END);
    }

    @Test
    @Transactional
    public void getAllTurns() throws Exception {
        // Initialize the database
        turnRepository.saveAndFlush(turn);

        // Get all the turns
        restTurnMockMvc.perform(get("/api/turns?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(turn.getId().intValue())))
                .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
                .andExpect(jsonPath("$.[*].started").value(hasItem(DEFAULT_START_STR)))
                .andExpect(jsonPath("$.[*].ended").value(hasItem(DEFAULT_END_STR)));
    }

    @Test
    @Transactional
    public void getTurn() throws Exception {
        // Initialize the database
        turnRepository.saveAndFlush(turn);

        // Get the turn
        restTurnMockMvc.perform(get("/api/turns/{id}", turn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(turn.getId().intValue()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.started").value(DEFAULT_START_STR))
            .andExpect(jsonPath("$.ended").value(DEFAULT_END_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTurn() throws Exception {
        // Get the turn
        restTurnMockMvc.perform(get("/api/turns/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTurn() throws Exception {
        // Initialize the database
        turnRepository.saveAndFlush(turn);

		int databaseSizeBeforeUpdate = turnRepository.findAll().size();

        // Update the turn
        turn.setActivated(UPDATED_ACTIVATED);
        turn.setStarted(UPDATED_START);
        turn.setEnded(UPDATED_END);

        restTurnMockMvc.perform(put("/api/turns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(turn)))
                .andExpect(status().isOk());

        // Validate the Turn in the database
        List<Turn> turns = turnRepository.findAll();
        assertThat(turns).hasSize(databaseSizeBeforeUpdate);
        Turn testTurn = turns.get(turns.size() - 1);
        assertThat(testTurn.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testTurn.getStarted()).isEqualTo(UPDATED_START);
        assertThat(testTurn.getEnded()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    public void deleteTurn() throws Exception {
        // Initialize the database
        turnRepository.saveAndFlush(turn);

		int databaseSizeBeforeDelete = turnRepository.findAll().size();

        // Get the turn
        restTurnMockMvc.perform(delete("/api/turns/{id}", turn.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Turn> turns = turnRepository.findAll();
        assertThat(turns).hasSize(databaseSizeBeforeDelete - 1);
    }
}
