package com.code_execution.code_executor.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(groupId = "java-code-executor-group", topics = "code-submission")
    public void consume(String message){
        System.out.println("Received Message: " + message);
    }
}
