package com.mycompany.myapp.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mycompany.myapp.domain.TradeMessage;

/**
 * Spring Data JPA repository for the TradeMessage entity.
 */
public interface TradeMessageRepository extends JpaRepository<TradeMessage, Long> {

	@Query("select tm.currencyFrom, sum(tm.amountSell), sum(tm.amountBuy) from TradeMessage tm group by tm.currencyFrom")
	List<Object> findByCurrencyFrom();

	@Query("select tm.currencyTo, sum(tm.amountSell), sum(tm.amountBuy) from TradeMessage tm group by tm.currencyTo")
	List<Object> findByCurrencyTo();

	@Query("select tm.currencyFrom, sum(tm.amountSell), sum(tm.amountBuy) from TradeMessage tm where tm.timePlaced between ?1 and ?2 group by tm.currencyFrom")
	List<Object> findByCurrencyFromBetween(ZonedDateTime fromDate, ZonedDateTime toDate);

	@Query("select tm.currencyTo, sum(tm.amountSell), sum(tm.amountBuy) from TradeMessage tm where tm.timePlaced between ?1 and ?2 group by tm.currencyTo")
	List<Object> findByCurrencyToBetween(ZonedDateTime fromDate, ZonedDateTime toDate);

}
