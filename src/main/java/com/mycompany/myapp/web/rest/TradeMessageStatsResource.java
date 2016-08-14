package com.mycompany.myapp.web.rest;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.service.TradeMessageService;

/**
 * REST controller for managing TradeMessage.
 */
@RestController
@RequestMapping("/api")
public class TradeMessageStatsResource {

	private final Logger log = LoggerFactory.getLogger(TradeMessageStatsResource.class);

	@Inject
	private TradeMessageService tradeMessageService;

	@RequestMapping(value = "/trade-messages-stats/currencyFrom", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Object>> getTradeMessagesGroupByCurrencyFrom(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime toDate)
			throws URISyntaxException {
		log.debug("REST request to get a page of TradeMessages");
		List<Object> results = null;
		if (fromDate == null || toDate == null) {
			results = tradeMessageService.findByCurrencyFrom();
		} else {
			results = tradeMessageService.findByCurrencyFromBetween(fromDate, toDate);
		}
		return new ResponseEntity<>(results, HttpStatus.OK);
	}

	@RequestMapping(value = "/trade-messages-stats/currencyTo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Object>> getTradeMessagesGroupByCurrencyTo(
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime toDate)
			throws URISyntaxException {
		log.debug("REST request to get a page of TradeMessages");
		List<Object> results = null;
		if (fromDate == null || toDate == null) {
			results = tradeMessageService.findByCurrencyTo();
		} else {
			results = tradeMessageService.findByCurrencyToBetween(fromDate, toDate);
		}
		return new ResponseEntity<>(results, HttpStatus.OK);
	}

}
