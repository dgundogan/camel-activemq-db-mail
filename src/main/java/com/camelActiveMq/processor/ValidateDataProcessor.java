package com.camelActiveMq.processor;


import com.camelActiveMq.domain.Item;
import com.camelActiveMq.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Component
@Slf4j
public class ValidateDataProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Item item = (Item) exchange.getIn().getBody();
        log.info("Item in the ValidateDataProcessor: "+item);

        if(ObjectUtils.isEmpty(item.getSkuu())){
            throw new DataException("Sku is null for : "+item.getItemDescription());
        }
    }
}
