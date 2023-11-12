package com.jeongyuneo.blogsearchservice.blogsearch.dto;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.kakaoapi.Document;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class BlogSearchResponse {

    private static final String EMPTY = "";

    private final List<Document> documents;
    private final String next;

    public static BlogSearchResponse empty() {
        return new BlogSearchResponse();
    }

    public static BlogSearchResponse from(List<Document> documents) {
        return new BlogSearchResponse(documents);
    }

    public static BlogSearchResponse of(List<Document> documents, String next) {
        return new BlogSearchResponse(documents, next);
    }

    private BlogSearchResponse() {
        this(Collections.emptyList());
    }

    private BlogSearchResponse(List<Document> documents) {
        this(documents, EMPTY);
    }

    private BlogSearchResponse(List<Document> documents, String next) {
        this.documents = documents;
        this.next = next;
    }
}
