package com.mycompany.myapp.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.domain.TradeMessage;
import com.mycompany.myapp.repository.TradeMessageRepository;
import com.mycompany.myapp.repository.search.TradeMessageSearchRepository;
import com.mycompany.myapp.web.rest.dto.TradeMessageDTO;
import com.mycompany.myapp.web.rest.mapper.TradeMessageMapper;

/**
 * Service Implementation for managing TradeMessage.
 */
@Service
@Transactional
public class TradeMessageService {

	private final Logger log = LoggerFactory.getLogger(TradeMessageService.class);

	@Inject
	private TradeMessageRepository tradeMessageRepository;

	@Inject
	private TradeMessageMapper tradeMessageMapper;

	@Inject
	private TradeMessageSearchRepository tradeMessageSearchRepository;

	@Inject
	SimpMessageSendingOperations messagingTemplate;

	/**
	 * Save a tradeMessage.
	 * 
	 * @param tradeMessageDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	@Async
	public TradeMessageDTO save(TradeMessageDTO tradeMessageDTO) {
		log.debug("Request to save TradeMessage : {}", tradeMessageDTO);
		TradeMessage tradeMessage = tradeMessageMapper.tradeMessageDTOToTradeMessage(tradeMessageDTO);
		tradeMessage = tradeMessageRepository.save(tradeMessage);
		TradeMessageDTO result = tradeMessageMapper.tradeMessageToTradeMessageDTO(tradeMessage);
		tradeMessageSearchRepository.save(tradeMessage);
		// Publish to websocket
		publish(result);
		return result;
	}

	@Async
	public void publish(TradeMessageDTO tradeMessageDTO) {
		log.debug("Request to publish TradeMessage : {}", tradeMessageDTO);
		messagingTemplate.convertAndSend("/topic/trading", tradeMessageDTO);
	}

	/**
	 * Get all the tradeMessages.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<TradeMessage> findAll(Pageable pageable) {
		log.debug("Request to get all TradeMessages");
		Page<TradeMessage> result = tradeMessageRepository.findAll(pageable);
		return result;
	}

	/**
	 * Get one tradeMessage by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public TradeMessageDTO findOne(Long id) {
		log.debug("Request to get TradeMessage : {}", id);
		TradeMessage tradeMessage = tradeMessageRepository.findOne(id);
		TradeMessageDTO tradeMessageDTO = tradeMessageMapper.tradeMessageToTradeMessageDTO(tradeMessage);
		return tradeMessageDTO;
	}

	/**
	 * Delete the tradeMessage by id.
	 * 
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete TradeMessage : {}", id);
		tradeMessageRepository.delete(id);
		tradeMessageSearchRepository.delete(id);
	}

	/**
	 * Search for the tradeMessage corresponding to the query.
	 *
	 * @param query
	 *            the query of the search
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<TradeMessage> search(String query, Pageable pageable) {
		log.debug("Request to search for a page of TradeMessages for query {}", query);
		return tradeMessageSearchRepository.search(queryStringQuery(query), pageable);
	}

	public List<Object> findByCurrencyFrom() {
		log.debug("Request to get TradeMessages between {} and {}");
		return tradeMessageRepository.findByCurrencyFrom();
	}

	public List<Object> findByCurrencyTo() {
		log.debug("Request to get TradeMessages between {} and {}");
		return tradeMessageRepository.findByCurrencyTo();
	}

	public List<Object> findByCurrencyFromBetween(ZonedDateTime fromDate, ZonedDateTime toDate) {
		log.debug("Request to get TradeMessages between {} and {}", fromDate, toDate);
		return tradeMessageRepository.findByCurrencyFromBetween(fromDate, toDate);
	}

	public List<Object> findByCurrencyToBetween(ZonedDateTime fromDate, ZonedDateTime toDate) {
		log.debug("Request to get TradeMessages between {} and {}", fromDate, toDate);
		return tradeMessageRepository.findByCurrencyToBetween(fromDate, toDate);
	}

	public List<Object> findByMarket() {
		log.debug("Request to get TradeMessages by market");
		return tradeMessageRepository.findByMarket();
	}

	public List<Object> findByMarket(ZonedDateTime fromDate, ZonedDateTime toDate) {
		log.debug("Request to get TradeMessages by market between {} and {}", fromDate, toDate);
		return tradeMessageRepository.findByMarket(fromDate, toDate);
	}

	public List<Object> findBySpecificMarket(String market) {
		log.debug("Request to get TradeMessages by market {}", market);
		return tradeMessageRepository.findBySpecificMarket(market);
	}

	public List<Object> findBySpecificMarket(String market, ZonedDateTime fromDate, ZonedDateTime toDate) {
		log.debug("Request to get TradeMessages by market {} between {} and {}", market, fromDate, toDate);
		return tradeMessageRepository.findBySpecificMarket(market, fromDate, toDate);
	}

	public List<Object> findByOriginatingCountry() {
		log.debug("Request to get TradeMessages by market");
		return tradeMessageRepository.findByOriginatingCountry();
	}

	public List<Object> findByOriginatingCountry(ZonedDateTime fromDate, ZonedDateTime toDate) {
		log.debug("Request to get TradeMessages by originating country between {} and {}", fromDate, toDate);
		return tradeMessageRepository.findByOriginatingCountry(fromDate, toDate);
	}

}
