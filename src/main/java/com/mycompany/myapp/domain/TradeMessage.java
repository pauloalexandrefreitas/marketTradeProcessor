package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A TradeMessage.
 */
@Entity
@Table(name = "trade_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "trademessage")
public class TradeMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "currency_from", length = 3, nullable = false)
    private String currencyFrom;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "currency_to", length = 3, nullable = false)
    private String currencyTo;

    @NotNull
    @Column(name = "amount_sell", precision=10, scale=2, nullable = false)
    private BigDecimal amountSell;

    @NotNull
    @Column(name = "amount_buy", precision=10, scale=2, nullable = false)
    private BigDecimal amountBuy;

    @NotNull
    @Column(name = "rate", precision=10, scale=2, nullable = false)
    private BigDecimal rate;

    @NotNull
    @Column(name = "time_placed", nullable = false)
    private ZonedDateTime timePlaced;

    @NotNull
    @Size(min = 2, max = 3)
    @Column(name = "originating_country", length = 3, nullable = false)
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
        TradeMessage tradeMessage = (TradeMessage) o;
        if(tradeMessage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tradeMessage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TradeMessage{" +
            "id=" + id +
            ", userId='" + userId + "'" +
            ", currencyFrom='" + currencyFrom + "'" +
            ", currencyTo='" + currencyTo + "'" +
            ", amountSell='" + amountSell + "'" +
            ", amountBuy='" + amountBuy + "'" +
            ", rate='" + rate + "'" +
            ", timePlaced='" + timePlaced + "'" +
            ", originatingCountry='" + originatingCountry + "'" +
            '}';
    }
}
