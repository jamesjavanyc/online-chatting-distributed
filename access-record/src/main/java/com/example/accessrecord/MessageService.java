package com.example.accessrecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RabbitListener(queues = {"tenant.questionanswer"})
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final LogMessageRepository logMessageRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitHandler
    public void consumer(String msg , Channel channel, Message message) throws IOException {
        LogMessage logMessage = objectMapper.readValue(msg, LogMessage.class);
        this.logMessageRepository.save(logMessage);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("Receive message:" + msg);
    }

    public List<LogMessage> getHistory(Long id){
//        this.logMessageRepository.deleteAll();
        return this.logMessageRepository.findByTenantId(id)
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }

}
