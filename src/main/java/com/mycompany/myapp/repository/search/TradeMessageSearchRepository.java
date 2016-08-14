package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.TradeMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TradeMessage entity.
 */
public interface TradeMessageSearchRepository extends ElasticsearchRepository<TradeMessage, Long> {
}
