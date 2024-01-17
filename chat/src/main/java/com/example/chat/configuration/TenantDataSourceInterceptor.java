package com.example.chat.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
@AllArgsConstructor
public class TenantDataSourceInterceptor implements HandlerInterceptor {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessageToQueue(String message) {
        rabbitTemplate.convertAndSend("uri_recording_exchange","routingKey",message);
        log.info("Send message to RabbitMQ:" + message);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantId = request.getParameter("tenant-id");
        if(StringUtils.hasLength(tenantId)){
            DataSourceContextHolder.setBranchContext(tenantId);
            sendMessageToQueue(String.format("{%s} has access by uri {%S}", tenantId, request.getRequestURI()));
            return true;
        }
        response.setStatus(403);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        DataSourceContextHolder.clearBranchContext();
    }
}
