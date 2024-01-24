package com.example.Musicalog.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Album(
        @Id String id,
        String title,
        String artistName,
        MediaType type,
        Integer stock,
        byte[] cover
) {
}
