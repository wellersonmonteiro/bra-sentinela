package com.projeto.complaintservice.config;

import com.projeto.complaintservice.controller.dto.ComplaintCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintProducer {
    
    private final RabbitTemplate rabbitTemplate;
    
    public void sendComplaintCreated(ComplaintCreateResponse event) {
        log.info("Sending complaint created event: {}", event.getProtocol());
        
        try {
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_PROTOCOL,
                event
            );
            
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_NOTIFICATION,
                event
            );
            
            log.info("Complaint created event sent successfully");
            
        } catch (Exception e) {
            log.error("Error sending complaint created event", e);
        }
    }
}
