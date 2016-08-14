package com.mycompany.myapp.web.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A DTO for the TradeMessage entity.
 */
public class TradeMessageDTO implements Serializable {

	private static final long serialVersionUID = 1492837880604526769L;

	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	@Size(min = 3, max = 3)
	private String currencyFrom;

	@NotNull
	@Size(min = 3, max = 3)
	private String currencyTo;

	@NotNull
	private BigDecimal amountSell;

	@NotNull
	private BigDecimal amountBuy;

	@NotNull
	private BigDecimal rate;

	@NotNull
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private ZonedDateTime timePlaced;

	@NotNull
	@Size(min = 2, max = 3)
	private String originatingCountry;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCurrencyFrom() {
		return currencyFrom;
	}

	public void setCurrencyFrom(String currencyFrom) {
		this.currencyFrom = currencyFrom;
	}

	public String getCurrencyTo() {
		return currencyTo;
	}

	public void setCurrencyTo(String currencyTo) {
		this.currencyTo = currencyTo;
	}

	public BigDecimal getAmountSell() {
		return amountSell;
	}

	public void setAmountSell(BigDecimal amountSell) {
		this.amountSell = amountSell;
	}

	public BigDecimal getAmountBuy() {
		return amountBuy;
	}

	public void setAmountBuy(BigDecimal amountBuy) {
		this.amountBuy = amountBuy;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public ZonedDateTime getTimePlaced() {
		return timePlaced;
	}

	public void setTimePlaced(ZonedDateTime timePlaced) {
		this.timePlaced = timePlaced;
	}

	public String getOriginatingCountry() {
		return originatingCountry;
	}

	public void setOriginatingCountry(String originatingCountry) {
		this.originatingCountry = originatingCountry;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TradeMessageDTO tradeMessageDTO = (TradeMessageDTO) o;

		if (!Objects.equals(id, tradeMessageDTO.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "TradeMessageDTO{" + "id=" + id + ", userId='" + userId + "'" + ", currencyFrom='" + currencyFrom + "'" + ", currencyTo='" + currencyTo + "'"
				+ ", amountSell='" + amountSell + "'" + ", amountBuy='" + amountBuy + "'" + ", rate='" + rate + "'" + ", timePlaced='" + timePlaced + "'"
				+ ", originatingCountry='" + originatingCountry + "'" + '}';
	}
}
