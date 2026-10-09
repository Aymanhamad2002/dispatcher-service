package com.polarbookshop.dispatcherservice.function;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.polarbookshop.dispatcherservice.message.OrderAcceptedMessage;
import com.polarbookshop.dispatcherservice.message.OrderDispatchedMessage;

import reactor.core.publisher.Flux;

@Configuration 
public class DispatcherFunctions {
    private static final Logger log = 
    LoggerFactory.getLogger(DispatcherFunctions.class);

    @Bean 
    public Function<OrderAcceptedMessage , Long> pack(){
        return orderAccepMessage ->{
            log.info("The order with id {} is packed.",
            orderAccepMessage.orderId());
            return orderAccepMessage.orderId();

        };
    }

    @Bean 
    public Function<Flux<Long>,Flux<OrderDispatchedMessage>> label(){
        return orderFlux -> orderFlux.map(orderId ->{
            log.info("The order with id {} is labeled.",orderId);
            return new OrderDispatchedMessage(orderId);
        }).doOnError(error -> log.error("Order labeling stream failed", error));
    }
    
}
