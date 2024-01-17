package com.example.chat.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    /**
     * 自定义消息队列
     */
    @Bean
    public Queue rabbitmqQueue(){
        return new Queue("queue",true,false,false);
    }

    /**
     * 自定义交换机
     */
    @Bean
    public DirectExchange rabbitmqDirectExchange(){
        return new DirectExchange("uri_recording_exchange",true,false);
    }

    /**
     * 关联绑定
     */
    @Bean
    public Binding bindDirect(){
        return BindingBuilder.bind(rabbitmqQueue()).to(rabbitmqDirectExchange()).with("recording.uri.chat");
    }

}
