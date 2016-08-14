package com.mycompany.myapp.web.rest;

import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.config.annotations.RateLimit;
import com.mycompany.myapp.service.TradeMessageService;
import com.mycompany.myapp.web.rest.dto.TradeMessageDTO;
import com.mycompany.myapp.web.rest.util.HeaderUtil;

import springfox.documentation.annotations.ApiIgnore;

/**
 * REST controller for managing TradeMessage.
 */
@RestController
@RequestMapping("/api")
public class TradeMessagePublicResource {

	private final Logger log = LoggerFactory.getLogger(TradeMessagePublicResource.class);

	@Inject
	private TradeMessageService tradeMessageService;

	@Inject
	SimpMessageSendingOperations messagingTemplate;

	/**
	 * POST /public/v1/trade-messages : Public endpoint to Create a new tradeMessage.
	 *
	 * @param tradeMessageDTO
	 *            the tradeMessageDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new tradeMessageDTO, or with status 400 (Bad Request) if the
	 *         tradeMessage has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/public/v1/trade-messages", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	@RateLimit(value = 1)
	public ResponseEntity<?> createTradeMessage(@Valid @RequestBody TradeMessageDTO tradeMessageDTO, @ApiIgnore HttpServletRequest request)
			throws URISyntaxException {
		log.debug("REST request to save TradeMessage : {}", tradeMessageDTO);
		if (tradeMessageDTO.getId() != null) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("tradeMessage", "idexists", "A new tradeMessage cannot already have an ID")).body(null);
		}
		tradeMessageService.save(tradeMessageDTO);
		// No need to return anything but the success status code
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
