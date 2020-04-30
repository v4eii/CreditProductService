package ru.neoflex.product.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface ApplicationSink {
    String INPUT = "input";
    String OUTPUT = "output";

    @Input(ApplicationSink.INPUT)
    SubscribableChannel input();
    @Output(ApplicationSink.OUTPUT)
    MessageChannel output();
}
