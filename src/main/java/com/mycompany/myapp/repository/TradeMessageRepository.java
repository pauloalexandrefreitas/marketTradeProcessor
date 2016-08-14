package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TradeMessage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TradeMessage entity.
 */
@SuppressWarnings("unused")
public interface TradeMessageRepository extends JpaRepository<TradeMessage,Long> {

}
