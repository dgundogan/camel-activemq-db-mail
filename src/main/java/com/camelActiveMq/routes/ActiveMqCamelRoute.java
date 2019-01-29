package com.camelActiveMq.routes;

import com.camelActiveMq.alert.MailProcessor;
import com.camelActiveMq.domain.Item;
import com.camelActiveMq.exception.DataException;
import com.camelActiveMq.processor.ValidateDataProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Created by z001qgd on 1/3/18.
 */
@Component
public class ActiveMqCamelRoute extends RouteBuilder{

    @Autowired
    Environment environment;

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Autowired
    MailProcessor mailProcessor;

    @Autowired
    ValidateDataProcessor validateDataProcessor;

    @Override
    public void configure() throws Exception {

        GsonDataFormat itemFormat = new GsonDataFormat(Item.class);

        onException(PSQLException.class).log(LoggingLevel.ERROR,"PSQLException in the route ${body}")
                .maximumRedeliveries(3).redeliveryDelay(3000).backOffMultiplier(2).retryAttemptedLogLevel(LoggingLevel.ERROR);

        onException(DataException.class,RuntimeException.class).log(LoggingLevel.ERROR, "DataException in the route ${body}")
                .process(mailProcessor)
                .to("{{errorQueue}}");


        from("{{fromRoute}}")
                    .log("Read message from activeMQ ${body}")
                .unmarshal(itemFormat)
                .log("Unmarshaled message is : ${body}")
                .process(validateDataProcessor)
                .to("{{toRoute}}");

        }
}
