package com.mycompany.myapp.web.rest.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.web.rest.dto.TradeMessageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TradeMessage and its DTO TradeMessageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TradeMessageMapper {

    TradeMessageDTO tradeMessageToTradeMessageDTO(TradeMessage tradeMessage);

    List<TradeMessageDTO> tradeMessagesToTradeMessageDTOs(List<TradeMessage> tradeMessages);

    TradeMessage tradeMessageDTOToTradeMessage(TradeMessageDTO tradeMessageDTO);

    List<TradeMessage> tradeMessageDTOsToTradeMessages(List<TradeMessageDTO> tradeMessageDTOs);
}
