package com.mycompany.myapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.TradeMessage;
import com.mycompany.myapp.service.TradeMessageService;
import com.mycompany.myapp.web.rest.dto.TradeMessageDTO;
import com.mycompany.myapp.web.rest.mapper.TradeMessageMapper;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;

/**
 * REST controller for managing TradeMessage.
 */
@RestController
@RequestMapping("/api")
public class TradeMessageResource {

	private final Logger log = LoggerFactory.getLogger(TradeMessageResource.class);

	@Inject
	private TradeMessageService tradeMessageService;

	@Inject
	private TradeMessageMapper tradeMessageMapper;

	/**
	 * POST /trade-messages : Create a new tradeMessage.
	 *
	 * @param tradeMessageDTO
	 *            the tradeMessageDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new tradeMessageDTO, or with status 400 (Bad Request) if the
	 *         tradeMessage has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/trade-messages", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<TradeMessageDTO> createTradeMessage(@Valid @RequestBody TradeMessageDTO tradeMessageDTO) throws URISyntaxException {
		log.debug("REST request to save TradeMessage : {}", tradeMessageDTO);
		if (tradeMessageDTO.getId() != null) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("tradeMessage", "idexists", "A new tradeMessage cannot already have an ID")).body(null);
		}
		TradeMessageDTO result = tradeMessageService.save(tradeMessageDTO);
		return ResponseEntity.created(new URI("/api/trade-messages/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("tradeMessage", result.getId().toString())).body(result);
	}

	/**
	 * PUT /trade-messages : Updates an existing tradeMessage.
	 *
	 * @param tradeMessageDTO
	 *            the tradeMessageDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated tradeMessageDTO, or with status 400 (Bad Request) if the
	 *         tradeMessageDTO is not valid, or with status 500 (Internal Server Error) if the tradeMessageDTO couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/trade-messages", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<TradeMessageDTO> updateTradeMessage(@Valid @RequestBody TradeMessageDTO tradeMessageDTO) throws URISyntaxException {
		log.debug("REST request to update TradeMessage : {}", tradeMessageDTO);
		if (tradeMessageDTO.getId() == null) {
			return createTradeMessage(tradeMessageDTO);
		}
		TradeMessageDTO result = tradeMessageService.save(tradeMessageDTO);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("tradeMessage", tradeMessageDTO.getId().toString())).body(result);
	}

	/**
	 * GET /trade-messages : get all the tradeMessages.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of tradeMessages in body
	 * @throws URISyntaxException
	 *             if there is an error to generate the pagination HTTP headers
	 */
	@RequestMapping(value = "/trade-messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<TradeMessageDTO>> getAllTradeMessages(Pageable pageable) throws URISyntaxException {
		log.debug("REST request to get a page of TradeMessages");
		Page<TradeMessage> page = tradeMessageService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trade-messages");
		return new ResponseEntity<>(tradeMessageMapper.tradeMessagesToTradeMessageDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET /trade-messages/:id : get the "id" tradeMessage.
	 *
	 * @param id
	 *            the id of the tradeMessageDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the tradeMessageDTO, or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/trade-messages/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<TradeMessageDTO> getTradeMessage(@PathVariable Long id) {
		log.debug("REST request to get TradeMessage : {}", id);
		TradeMessageDTO tradeMessageDTO = tradeMessageService.findOne(id);
		return Optional.ofNullable(tradeMessageDTO).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /trade-messages/:id : delete the "id" tradeMessage.
	 *
	 * @param id
	 *            the id of the tradeMessageDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/trade-messages/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteTradeMessage(@PathVariable Long id) {
		log.debug("REST request to delete TradeMessage : {}", id);
		tradeMessageService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tradeMessage", id.toString())).build();
	}

	/**
	 * SEARCH /_search/trade-messages?query=:query : search for the tradeMessage corresponding to the query.
	 *
	 * @param query
	 *            the query of the tradeMessage search
	 * @return the result of the search
	 */
	@RequestMapping(value = "/_search/trade-messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<TradeMessageDTO>> searchTradeMessages(@RequestParam String query, Pageable pageable) throws URISyntaxException {
		log.debug("REST request to search for a page of TradeMessages for query {}", query);
		Page<TradeMessage> page = tradeMessageService.search(query, pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/trade-messages");
		
		List<Object> results = tradeMessageService.findByCurrencyFromBetween(null, null);
		System.out.println(results);
		return new ResponseEntity<>(tradeMessageMapper.tradeMessagesToTradeMessageDTOs(page.getContent()), headers, HttpStatus.OK);
	}

}
