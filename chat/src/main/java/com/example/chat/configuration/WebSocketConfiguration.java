package com.example.chat.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @Description EnableWebSocketMessageBroker-注解开启STOMP协议来传输基于代理的消息，此时控制器支持使用@MessageMapping
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/fanout-channel", "/tenant-channel");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册两个STOMP的endpoint，分别用于广播和点对点
        //广播
        registry.addEndpoint("/chat/socket/public").setAllowedOrigins("*");
        //点对点
        registry.addEndpoint("/chat/socket/tenant").setAllowedOrigins("*");
    }
}
