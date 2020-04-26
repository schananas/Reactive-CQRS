package com.github.stefanvozd.cqrs.reactiveaxon.rest.projection;

import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;


@RestController
public class ResetToken {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Configuration configuration;

    public ResetToken(Configuration configuration) {
        this.configuration = configuration;
    }

    @GetMapping("/reset")
    public void triggerResetEventEndpointFor() {

        configuration.eventProcessingConfiguration()
                .eventProcessors()
                .values()
                .stream()
                .filter(eventProcessor -> eventProcessor instanceof TrackingEventProcessor)
                .map(eventProcessor -> ((TrackingEventProcessor) eventProcessor))
                .forEach((trackingEventProcessor) -> {

                    logger.info("Shutdown down {}", trackingEventProcessor.getName());
                    trackingEventProcessor.shutDown();
                    logger.info("{} is shutdown down", trackingEventProcessor.getName());

                    logger.info("Restarting token for {}", trackingEventProcessor.getName());
                    trackingEventProcessor.resetTokens();
                    logger.info("Token restarted for {}", trackingEventProcessor.getName());


                    logger.info("Starting tracking processor {}", trackingEventProcessor.getName());
                    trackingEventProcessor.start();
                    logger.info("Tracking processor started {}", trackingEventProcessor.getName());

                });
    }

}