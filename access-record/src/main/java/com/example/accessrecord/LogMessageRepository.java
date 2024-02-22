package com.example.accessrecord;

import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface LogMessageRepository extends ElasticsearchRepository<LogMessage, String> {
    @Highlight(fields = {
            @HighlightField(name = "tenant_id"),
            @HighlightField(name = "question")
    })
    @Query("{\"match\":{\"tenant_id\":\"?0\"}}")
    SearchHits<LogMessage> findByTenantId(Long tenantId);
}
