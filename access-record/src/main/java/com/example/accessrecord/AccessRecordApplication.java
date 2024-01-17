package com.example.accessrecord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@RabbitListener(queues = {"queue"})
@Slf4j
public class AccessRecordApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessRecordApplication.class, args);
	}

	@RabbitHandler
	public void consumer(String message) {
		log.info("获取到队列中消息：" + message);
	}
}
