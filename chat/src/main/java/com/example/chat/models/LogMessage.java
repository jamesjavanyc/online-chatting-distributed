package com.example.chat.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

public record LogMessage(String tenantId,
                         String question,
                         @JsonSerialize(using = LocalDateTimeSerializer.class)
                         @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                         LocalDateTime datetime
) implements Serializable {
}
