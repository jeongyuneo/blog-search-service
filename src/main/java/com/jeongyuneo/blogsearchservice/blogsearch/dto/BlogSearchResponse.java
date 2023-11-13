package com.jeongyuneo.blogsearchservice.blogsearch.dto;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class BlogSearchResponse {

    private static final String EMPTY = "";

    private final List<BlogSearchResponseElement> documents;
    private final String next;

    public static BlogSearchResponse empty() {
        return new BlogSearchResponse();
    }

    public static BlogSearchResponse from(List<BlogSearchResponseElement> documents) {
        return new BlogSearchResponse(documents);
    }

    public static BlogSearchResponse of(List<BlogSearchResponseElement> documents, String next) {
        return new BlogSearchResponse(documents, next);
    }

    private BlogSearchResponse() {
        this(Collections.emptyList());
    }

    private BlogSearchResponse(List<BlogSearchResponseElement> documents) {
        this(documents, EMPTY);
    }

    private BlogSearchResponse(List<BlogSearchResponseElement> documents, String next) {
        this.documents = documents;
        this.next = next;
    }
}
