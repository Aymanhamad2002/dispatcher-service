package com.polarbookshop.dispatcherservice;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;

import com.polarbookshop.dispatcherservice.message.OrderAcceptedMessage;
import com.polarbookshop.dispatcherservice.message.OrderDispatchedMessage;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@FunctionalSpringBootTest 
public class DispatchingFunctionsIntegrationTests {
    @Autowired 
    private FunctionCatalog functionCatalog;

    @Test 
    void packAndLabelOrder(){
        Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>> packAndLabel =
        functionCatalog.lookup(Function.class,"pack|label");
            long orderId =  123;

            StepVerifier.create(packAndLabel.apply(new OrderAcceptedMessage(orderId)))
            .expectNextMatches(dispatcherOrder -> 
                dispatcherOrder.equals(new OrderDispatchedMessage(orderId))
                
            )
            .verifyComplete();

        
    }
}
