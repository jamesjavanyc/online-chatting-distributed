package com.example.accessrecord;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfigurationSupport {
    @Bean
    @Override
    @Primary
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        List<Converter> converters = new ArrayList<>();
        converters.add(LongToLocalDateTimeConverter.INSTANCE);
        return new ElasticsearchCustomConversions(converters);
    }

    @ReadingConverter
    enum LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {
        INSTANCE;
        @Override
        public LocalDateTime convert(Long source) {
            return Instant.ofEpochMilli(source).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }



}
