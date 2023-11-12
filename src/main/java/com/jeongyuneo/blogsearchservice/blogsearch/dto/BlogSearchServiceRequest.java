package com.jeongyuneo.blogsearchservice.blogsearch.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogSearchServiceRequest {

    private String query;
    private String sort;
    private int page;
    private boolean isMaxPage;

    public static BlogSearchServiceRequest of(String query, String sort, int page, boolean isMaxPage) {
        return new BlogSearchServiceRequest(query, sort, page, isMaxPage);
    }
}
