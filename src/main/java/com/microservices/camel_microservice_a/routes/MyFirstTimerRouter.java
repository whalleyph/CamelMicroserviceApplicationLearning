package com.microservices.camel_microservice_a.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import org.slf4j.Logger;

//@Component
public class MyFirstTimerRouter extends RouteBuilder {

    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;

    @Autowired
    private SimpleLoggingProcessingComponent loggingComponent;

    @Override
    public void configure() throws Exception {
        from("timer:first-timer")
                .log("${body}")
                .transform().constant("My Constant Message")
                .log("${body}")
                .bean(getCurrentTimeBean)
                .log("${body}")
                .bean(loggingComponent)
                .log("${body}")
                .process(new SimpleLoggingProcessor())
        .to("log:first-timer");
    }
}

@Component
class GetCurrentTimeBean {
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}

@Component
class SimpleLoggingProcessingComponent {
    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessingComponent.class);
    public void process(String message) {
        logger.info("SimpleLoggingProcessingComponent {}", message);
    }
}

class SimpleLoggingProcessor implements Processor {

    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("SimpleLoggingProcessingComponent {}", exchange.getMessage().getBody());
    }
}