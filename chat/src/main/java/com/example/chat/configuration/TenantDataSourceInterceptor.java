package com.example.chat.configuration;

import com.example.chat.models.LogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Slf4j
@Component
@AllArgsConstructor
public class TenantDataSourceInterceptor implements HandlerInterceptor {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendMessageToQueue(Object message) throws JsonProcessingException {
        rabbitTemplate.convertAndSend("recording_exchange","chat.content", objectMapper.writeValueAsString(message));
        log.info("Send message to RabbitMQ:" + objectMapper.writeValueAsString(message));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantId = request.getParameter("tenant-id");
        if(StringUtils.hasLength(tenantId)){
            DataSourceContextHolder.setBranchContext(tenantId);
            return true;
        }
        response.setStatus(403);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String tenantId = request.getParameter("tenant-id");
        if(StringUtils.hasLength(tenantId) && response.getStatus() == 200){
            if("/chat".equalsIgnoreCase(request.getRequestURI())){
                String question = request.getParameter("text");
                sendMessageToQueue(new LogMessage(tenantId, question, LocalDateTime.now()));
            }
        }
        DataSourceContextHolder.clearBranchContext();
    }
}
