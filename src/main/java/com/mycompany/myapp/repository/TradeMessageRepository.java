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

	@Query("select tm.currencyFrom, sum(tm.amountSell), sum(tm.amountBuy), avg(tm.rate) from TradeMessage tm group by tm.currencyFrom")
	List<Object> findByCurrencyFrom();

	@Query("select tm.currencyTo, sum(tm.amountSell), sum(tm.amountBuy), avg(tm.rate) from TradeMessage tm group by tm.currencyTo")
	List<Object> findByCurrencyTo();

	@Query("select tm.currencyFrom, sum(tm.amountSell), sum(tm.amountBuy), avg(tm.rate) from TradeMessage tm where tm.timePlaced between ?1 and ?2 group by tm.currencyFrom")
	List<Object> findByCurrencyFromBetween(ZonedDateTime fromDate, ZonedDateTime toDate);

	@Query("select tm.currencyTo, sum(tm.amountSell), sum(tm.amountBuy), avg(tm.rate) from TradeMessage tm where tm.timePlaced between ?1 and ?2 group by tm.currencyTo")
	List<Object> findByCurrencyToBetween(ZonedDateTime fromDate, ZonedDateTime toDate);

	@Query("select concat(tm.currencyFrom,'->',tm.currencyTo) as market, sum(tm.amountSell), sum(tm.amountBuy), avg(tm.rate) from TradeMessage tm group by concat(tm.currencyFrom,tm.currencyTo)")
	List<Object> findByMarket();

	@Query("select concat(tm.currencyFrom,'->',tm.currencyTo) as market, sum(tm.amountSell), sum(tm.amountBuy), avg(tm.rate) from TradeMessage tm where tm.timePlaced between ?1 and ?2 group by concat(tm.currencyFrom,tm.currencyTo)")
	List<Object> findByMarket(ZonedDateTime fromDate, ZonedDateTime toDate);

	@Query("select concat(tm.currencyFrom,'->',tm.currencyTo) as market, sum(tm.amountSell), sum(tm.amountBuy), avg(tm.rate) from TradeMessage tm where concat(tm.currencyFrom,'->',tm.currencyTo) = ?1 group by concat(tm.currencyFrom,tm.currencyTo)")
	List<Object> findBySpecificMarket(String market);

	@Query("select concat(tm.currencyFrom,'->',tm.currencyTo) as market, sum(tm.amountSell), sum(tm.amountBuy), avg(tm.rate) from TradeMessage tm where concat(tm.currencyFrom,'->',tm.currencyTo) = ?1 AND tm.timePlaced between ?2 and ?3 group by concat(tm.currencyFrom,tm.currencyTo)")
	List<Object> findBySpecificMarket(String market, ZonedDateTime fromDate, ZonedDateTime toDate);

	@Query("select tm.originatingCountry, sum(tm.amountSell), sum(tm.amountBuy), avg(tm.rate) from TradeMessage tm group by originatingCountry")
	List<Object> findByOriginatingCountry();

	@Query("select tm.originatingCountry, sum(tm.amountSell), sum(tm.amountBuy), avg(tm.rate) from TradeMessage tm where tm.timePlaced between ?1 and ?2 group by tm.originatingCountry")
	List<Object> findByOriginatingCountry(ZonedDateTime fromDate, ZonedDateTime toDate);

}
