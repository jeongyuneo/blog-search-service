package com.jeongyuneo.blogsearchservice.blogsearch.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogSearchRankingResponse {

    private List<BlogSearchRankingResponseElement> blogSearchRankings;

    public static BlogSearchRankingResponse from(List<BlogSearchRankingResponseElement> blogSearchRankings) {
        return new BlogSearchRankingResponse(blogSearchRankings);
    }
}
