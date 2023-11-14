package com.jeongyuneo.blogsearchservice.blogsearch.controller;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchResponse;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchResponseElement;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchServiceRequest;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.kakaoapi.Document;
import com.jeongyuneo.blogsearchservice.blogsearch.service.BlogSearchRankingService;
import com.jeongyuneo.blogsearchservice.blogsearch.service.BlogSearchService;
import com.jeongyuneo.blogsearchservice.global.support.ApiDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("블로그 검색 컨트롤러 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(BlogSearchController.class)
class BlogSearchControllerTest extends ApiDocument {

    private static final String BLOG_SEARCH_ROOT_URI = "/blogs/search";

    @MockBean
    private BlogSearchService blogSearchService;

    @MockBean
    private BlogSearchRankingService blogSearchRankingService;

    @Test
    void 키워드로_검색하면_다음페이지를_포함한_검색결과를_반환한다() throws Exception {
        // given
        Document document = Document.builder()
                .title("블로그 글 제목")
                .contents("블로그 글 요약")
                .url("블로그 글 url")
                .blogname("블로그 이름")
                .thumbnail("검색 시스템에서 추출한 대표 미리보기 이미지 URL")
                .datetime("2023-11-11T10:30:000+09:00")
                .build();
        List<BlogSearchResponseElement> blogSearchResponseElements = IntStream.range(1, 5)
                .mapToObj(n -> BlogSearchResponseElement.from(document))
                .collect(Collectors.toList());
        String next = BLOG_SEARCH_ROOT_URI + "?query=keyword&sort=accuracy&page=2";
        BlogSearchResponse blogSearchResponse = BlogSearchResponse.of(blogSearchResponseElements, next);
        System.out.println(blogSearchResponse.getDocuments().get(0).getCreatedAt());
        willReturn(blogSearchResponse).given(blogSearchService).searchByQuery(any(BlogSearchServiceRequest.class));
        // when
        ResultActions resultActions = 블로그_검색을_요청한다();
        // then
        블로그_검색_요청이_성공한다(resultActions, blogSearchResponse);
    }

    private ResultActions 블로그_검색을_요청한다() throws Exception {
        return mockMvc.perform(get(CONTEXT_PATH + BLOG_SEARCH_ROOT_URI)
                .contextPath(CONTEXT_PATH)
                .queryParam("query", "keyword")
        );
    }

    private void 블로그_검색_요청이_성공한다(ResultActions resultActions, BlogSearchResponse blogSearchResponse) throws Exception {
        printAndMakeSnippet(resultActions
                        .andExpect(status().isOk())
                        .andExpect(content().json(toJson(blogSearchResponse))),
                "blog-search-success"
        );
    }
}
