package com.example.chat.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceRouting extends AbstractRoutingDataSource {

    private RestTemplate restTemplate;

    @Autowired
    public DataSourceRouting(DataSourceProperties dataSourceProperties) {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(buildDataSource(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword()));
    }


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(15000);
        this.restTemplate = new RestTemplate(factory);
        return this.restTemplate;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String tenantId = DataSourceContextHolder.getBranchContext();
        if (StringUtils.hasLength(tenantId) && !this.getResolvedDataSources().containsKey(tenantId) ) {
            ResponseEntity<DataSourceProperties> response = this.restTemplate.getForEntity("http://tenant-service/tenant?id=" + tenantId, DataSourceProperties.class);
            DataSourceProperties properties = response.getBody();
            addDataSource(DataSourceContextHolder.getBranchContext(), properties.getUrl(), properties.getUsername(), properties.getPassword());
        }
        return tenantId;
    }

    public void addDataSource(String tenantId, String url, String username, String password){
        addDataSource(tenantId, buildDataSource(url, username, password));
    }

    private void addDataSource(String tenantId, DataSource dataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(super.getResolvedDataSources());
        targetDataSources.put(tenantId, dataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
        super.initialize();
    }

    private DataSource buildDataSource(String url, String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}