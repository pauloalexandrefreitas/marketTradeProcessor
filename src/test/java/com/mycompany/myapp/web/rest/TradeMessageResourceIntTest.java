package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MarketTradeProcessorApp;
import com.mycompany.myapp.domain.TradeMessage;
import com.mycompany.myapp.repository.TradeMessageRepository;
import com.mycompany.myapp.service.TradeMessageService;
import com.mycompany.myapp.repository.search.TradeMessageSearchRepository;
import com.mycompany.myapp.web.rest.dto.TradeMessageDTO;
import com.mycompany.myapp.web.rest.mapper.TradeMessageMapper;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TradeMessageResource REST controller.
 *
 * @see TradeMessageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MarketTradeProcessorApp.class)
@WebAppConfiguration
@IntegrationTest
public class TradeMessageResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final String DEFAULT_CURRENCY_FROM = "AAA";
    private static final String UPDATED_CURRENCY_FROM = "BBB";
    private static final String DEFAULT_CURRENCY_TO = "AAA";
    private static final String UPDATED_CURRENCY_TO = "BBB";

    private static final BigDecimal DEFAULT_AMOUNT_SELL = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_SELL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AMOUNT_BUY = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_BUY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RATE = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_TIME_PLACED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME_PLACED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_PLACED_STR = dateTimeFormatter.format(DEFAULT_TIME_PLACED);
    private static final String DEFAULT_ORIGINATING_COUNTRY = "AA";
    private static final String UPDATED_ORIGINATING_COUNTRY = "BB";

    @Inject
    private TradeMessageRepository tradeMessageRepository;

    @Inject
    private TradeMessageMapper tradeMessageMapper;

    @Inject
    private TradeMessageService tradeMessageService;

    @Inject
    private TradeMessageSearchRepository tradeMessageSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTradeMessageMockMvc;

    private TradeMessage tradeMessage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TradeMessageResource tradeMessageResource = new TradeMessageResource();
        ReflectionTestUtils.setField(tradeMessageResource, "tradeMessageService", tradeMessageService);
        ReflectionTestUtils.setField(tradeMessageResource, "tradeMessageMapper", tradeMessageMapper);
        this.restTradeMessageMockMvc = MockMvcBuilders.standaloneSetup(tradeMessageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tradeMessageSearchRepository.deleteAll();
        tradeMessage = new TradeMessage();
        tradeMessage.setUserId(DEFAULT_USER_ID);
        tradeMessage.setCurrencyFrom(DEFAULT_CURRENCY_FROM);
        tradeMessage.setCurrencyTo(DEFAULT_CURRENCY_TO);
        tradeMessage.setAmountSell(DEFAULT_AMOUNT_SELL);
        tradeMessage.setAmountBuy(DEFAULT_AMOUNT_BUY);
        tradeMessage.setRate(DEFAULT_RATE);
        tradeMessage.setTimePlaced(DEFAULT_TIME_PLACED);
        tradeMessage.setOriginatingCountry(DEFAULT_ORIGINATING_COUNTRY);
    }

    @Test
    @Transactional
    public void createTradeMessage() throws Exception {
        int databaseSizeBeforeCreate = tradeMessageRepository.findAll().size();

        // Create the TradeMessage
        TradeMessageDTO tradeMessageDTO = tradeMessageMapper.tradeMessageToTradeMessageDTO(tradeMessage);

        restTradeMessageMockMvc.perform(post("/api/trade-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeMessageDTO)))
                .andExpect(status().isCreated());

        // Validate the TradeMessage in the database
        List<TradeMessage> tradeMessages = tradeMessageRepository.findAll();
        assertThat(tradeMessages).hasSize(databaseSizeBeforeCreate + 1);
        TradeMessage testTradeMessage = tradeMessages.get(tradeMessages.size() - 1);
        assertThat(testTradeMessage.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testTradeMessage.getCurrencyFrom()).isEqualTo(DEFAULT_CURRENCY_FROM);
        assertThat(testTradeMessage.getCurrencyTo()).isEqualTo(DEFAULT_CURRENCY_TO);
        assertThat(testTradeMessage.getAmountSell()).isEqualTo(DEFAULT_AMOUNT_SELL);
        assertThat(testTradeMessage.getAmountBuy()).isEqualTo(DEFAULT_AMOUNT_BUY);
        assertThat(testTradeMessage.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testTradeMessage.getTimePlaced()).isEqualTo(DEFAULT_TIME_PLACED);
        assertThat(testTradeMessage.getOriginatingCountry()).isEqualTo(DEFAULT_ORIGINATING_COUNTRY);

        // Validate the TradeMessage in ElasticSearch
        TradeMessage tradeMessageEs = tradeMessageSearchRepository.findOne(testTradeMessage.getId());
        assertThat(tradeMessageEs).isEqualToComparingFieldByField(testTradeMessage);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeMessageRepository.findAll().size();
        // set the field null
        tradeMessage.setUserId(null);

        // Create the TradeMessage, which fails.
        TradeMessageDTO tradeMessageDTO = tradeMessageMapper.tradeMessageToTradeMessageDTO(tradeMessage);

        restTradeMessageMockMvc.perform(post("/api/trade-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeMessageDTO)))
                .andExpect(status().isBadRequest());

        List<TradeMessage> tradeMessages = tradeMessageRepository.findAll();
        assertThat(tradeMessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrencyFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeMessageRepository.findAll().size();
        // set the field null
        tradeMessage.setCurrencyFrom(null);

        // Create the TradeMessage, which fails.
        TradeMessageDTO tradeMessageDTO = tradeMessageMapper.tradeMessageToTradeMessageDTO(tradeMessage);

        restTradeMessageMockMvc.perform(post("/api/trade-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeMessageDTO)))
                .andExpect(status().isBadRequest());

        List<TradeMessage> tradeMessages = tradeMessageRepository.findAll();
        assertThat(tradeMessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrencyToIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeMessageRepository.findAll().size();
        // set the field null
        tradeMessage.setCurrencyTo(null);

        // Create the TradeMessage, which fails.
        TradeMessageDTO tradeMessageDTO = tradeMessageMapper.tradeMessageToTradeMessageDTO(tradeMessage);

        restTradeMessageMockMvc.perform(post("/api/trade-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeMessageDTO)))
                .andExpect(status().isBadRequest());

        List<TradeMessage> tradeMessages = tradeMessageRepository.findAll();
        assertThat(tradeMessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountSellIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeMessageRepository.findAll().size();
        // set the field null
        tradeMessage.setAmountSell(null);

        // Create the TradeMessage, which fails.
        TradeMessageDTO tradeMessageDTO = tradeMessageMapper.tradeMessageToTradeMessageDTO(tradeMessage);

        restTradeMessageMockMvc.perform(post("/api/trade-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeMessageDTO)))
                .andExpect(status().isBadRequest());

        List<TradeMessage> tradeMessages = tradeMessageRepository.findAll();
        assertThat(tradeMessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountBuyIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeMessageRepository.findAll().size();
        // set the field null
        tradeMessage.setAmountBuy(null);

        // Create the TradeMessage, which fails.
        TradeMessageDTO tradeMessageDTO = tradeMessageMapper.tradeMessageToTradeMessageDTO(tradeMessage);

        restTradeMessageMockMvc.perform(post("/api/trade-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeMessageDTO)))
                .andExpect(status().isBadRequest());

        List<TradeMessage> tradeMessages = tradeMessageRepository.findAll();
        assertThat(tradeMessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeMessageRepository.findAll().size();
        // set the field null
        tradeMessage.setRate(null);

        // Create the TradeMessage, which fails.
        TradeMessageDTO tradeMessageDTO = tradeMessageMapper.tradeMessageToTradeMessageDTO(tradeMessage);

        restTradeMessageMockMvc.perform(post("/api/trade-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeMessageDTO)))
                .andExpect(status().isBadRequest());

        List<TradeMessage> tradeMessages = tradeMessageRepository.findAll();
        assertThat(tradeMessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimePlacedIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeMessageRepository.findAll().size();
        // set the field null
        tradeMessage.setTimePlaced(null);

        // Create the TradeMessage, which fails.
        TradeMessageDTO tradeMessageDTO = tradeMessageMapper.tradeMessageToTradeMessageDTO(tradeMessage);

        restTradeMessageMockMvc.perform(post("/api/trade-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeMessageDTO)))
                .andExpect(status().isBadRequest());

        List<TradeMessage> tradeMessages = tradeMessageRepository.findAll();
        assertThat(tradeMessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOriginatingCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeMessageRepository.findAll().size();
        // set the field null
        tradeMessage.setOriginatingCountry(null);

        // Create the TradeMessage, which fails.
        TradeMessageDTO tradeMessageDTO = tradeMessageMapper.tradeMessageToTradeMessageDTO(tradeMessage);

        restTradeMessageMockMvc.perform(post("/api/trade-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeMessageDTO)))
                .andExpect(status().isBadRequest());

        List<TradeMessage> tradeMessages = tradeMessageRepository.findAll();
        assertThat(tradeMessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTradeMessages() throws Exception {
        // Initialize the database
        tradeMessageRepository.saveAndFlush(tradeMessage);

        // Get all the tradeMessages
        restTradeMessageMockMvc.perform(get("/api/trade-messages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tradeMessage.getId().intValue())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$.[*].currencyFrom").value(hasItem(DEFAULT_CURRENCY_FROM.toString())))
                .andExpect(jsonPath("$.[*].currencyTo").value(hasItem(DEFAULT_CURRENCY_TO.toString())))
                .andExpect(jsonPath("$.[*].amountSell").value(hasItem(DEFAULT_AMOUNT_SELL.intValue())))
                .andExpect(jsonPath("$.[*].amountBuy").value(hasItem(DEFAULT_AMOUNT_BUY.intValue())))
                .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
                .andExpect(jsonPath("$.[*].timePlaced").value(hasItem(DEFAULT_TIME_PLACED_STR)))
                .andExpect(jsonPath("$.[*].originatingCountry").value(hasItem(DEFAULT_ORIGINATING_COUNTRY.toString())));
    }

    @Test
    @Transactional
    public void getTradeMessage() throws Exception {
        // Initialize the database
        tradeMessageRepository.saveAndFlush(tradeMessage);

        // Get the tradeMessage
        restTradeMessageMockMvc.perform(get("/api/trade-messages/{id}", tradeMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tradeMessage.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.currencyFrom").value(DEFAULT_CURRENCY_FROM.toString()))
            .andExpect(jsonPath("$.currencyTo").value(DEFAULT_CURRENCY_TO.toString()))
            .andExpect(jsonPath("$.amountSell").value(DEFAULT_AMOUNT_SELL.intValue()))
            .andExpect(jsonPath("$.amountBuy").value(DEFAULT_AMOUNT_BUY.intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()))
            .andExpect(jsonPath("$.timePlaced").value(DEFAULT_TIME_PLACED_STR))
            .andExpect(jsonPath("$.originatingCountry").value(DEFAULT_ORIGINATING_COUNTRY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTradeMessage() throws Exception {
        // Get the tradeMessage
        restTradeMessageMockMvc.perform(get("/api/trade-messages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTradeMessage() throws Exception {
        // Initialize the database
        tradeMessageRepository.saveAndFlush(tradeMessage);
        tradeMessageSearchRepository.save(tradeMessage);
        int databaseSizeBeforeUpdate = tradeMessageRepository.findAll().size();

        // Update the tradeMessage
        TradeMessage updatedTradeMessage = new TradeMessage();
        updatedTradeMessage.setId(tradeMessage.getId());
        updatedTradeMessage.setUserId(UPDATED_USER_ID);
        updatedTradeMessage.setCurrencyFrom(UPDATED_CURRENCY_FROM);
        updatedTradeMessage.setCurrencyTo(UPDATED_CURRENCY_TO);
        updatedTradeMessage.setAmountSell(UPDATED_AMOUNT_SELL);
        updatedTradeMessage.setAmountBuy(UPDATED_AMOUNT_BUY);
        updatedTradeMessage.setRate(UPDATED_RATE);
        updatedTradeMessage.setTimePlaced(UPDATED_TIME_PLACED);
        updatedTradeMessage.setOriginatingCountry(UPDATED_ORIGINATING_COUNTRY);
        TradeMessageDTO tradeMessageDTO = tradeMessageMapper.tradeMessageToTradeMessageDTO(updatedTradeMessage);

        restTradeMessageMockMvc.perform(put("/api/trade-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tradeMessageDTO)))
                .andExpect(status().isOk());

        // Validate the TradeMessage in the database
        List<TradeMessage> tradeMessages = tradeMessageRepository.findAll();
        assertThat(tradeMessages).hasSize(databaseSizeBeforeUpdate);
        TradeMessage testTradeMessage = tradeMessages.get(tradeMessages.size() - 1);
        assertThat(testTradeMessage.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testTradeMessage.getCurrencyFrom()).isEqualTo(UPDATED_CURRENCY_FROM);
        assertThat(testTradeMessage.getCurrencyTo()).isEqualTo(UPDATED_CURRENCY_TO);
        assertThat(testTradeMessage.getAmountSell()).isEqualTo(UPDATED_AMOUNT_SELL);
        assertThat(testTradeMessage.getAmountBuy()).isEqualTo(UPDATED_AMOUNT_BUY);
        assertThat(testTradeMessage.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testTradeMessage.getTimePlaced()).isEqualTo(UPDATED_TIME_PLACED);
        assertThat(testTradeMessage.getOriginatingCountry()).isEqualTo(UPDATED_ORIGINATING_COUNTRY);

        // Validate the TradeMessage in ElasticSearch
        TradeMessage tradeMessageEs = tradeMessageSearchRepository.findOne(testTradeMessage.getId());
        assertThat(tradeMessageEs).isEqualToComparingFieldByField(testTradeMessage);
    }

    @Test
    @Transactional
    public void deleteTradeMessage() throws Exception {
        // Initialize the database
        tradeMessageRepository.saveAndFlush(tradeMessage);
        tradeMessageSearchRepository.save(tradeMessage);
        int databaseSizeBeforeDelete = tradeMessageRepository.findAll().size();

        // Get the tradeMessage
        restTradeMessageMockMvc.perform(delete("/api/trade-messages/{id}", tradeMessage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean tradeMessageExistsInEs = tradeMessageSearchRepository.exists(tradeMessage.getId());
        assertThat(tradeMessageExistsInEs).isFalse();

        // Validate the database is empty
        List<TradeMessage> tradeMessages = tradeMessageRepository.findAll();
        assertThat(tradeMessages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTradeMessage() throws Exception {
        // Initialize the database
        tradeMessageRepository.saveAndFlush(tradeMessage);
        tradeMessageSearchRepository.save(tradeMessage);

        // Search the tradeMessage
        restTradeMessageMockMvc.perform(get("/api/_search/trade-messages?query=id:" + tradeMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tradeMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].currencyFrom").value(hasItem(DEFAULT_CURRENCY_FROM.toString())))
            .andExpect(jsonPath("$.[*].currencyTo").value(hasItem(DEFAULT_CURRENCY_TO.toString())))
            .andExpect(jsonPath("$.[*].amountSell").value(hasItem(DEFAULT_AMOUNT_SELL.intValue())))
            .andExpect(jsonPath("$.[*].amountBuy").value(hasItem(DEFAULT_AMOUNT_BUY.intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
            .andExpect(jsonPath("$.[*].timePlaced").value(hasItem(DEFAULT_TIME_PLACED_STR)))
            .andExpect(jsonPath("$.[*].originatingCountry").value(hasItem(DEFAULT_ORIGINATING_COUNTRY.toString())));
    }
}
