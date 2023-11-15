package com.jeongyuneo.blogsearchservice.blogsearch.service;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchResponse;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchServiceRequest;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.event.IncreaseBlogSearchCountEvent;
import com.jeongyuneo.blogsearchservice.global.util.FileReader;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@DisplayName("카카오 블로그 검색 서비스 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@RestClientTest(KakaoBlogSearchService.class)
class KakaoBlogSearchServiceTest {

    private static final String KAKAO_BLOG_SEARCH_URL = "https://dapi.kakao.com/v2/search/blog";
    private static final String BLOG_SEARCH_URL = "http://localhost:8080/api/v1/blogs/search";
    private static final int MAX_PAGE = 50;
    private static final int MAX_SIZE_OF_PAGE = 10;

    @MockBean
    private KakaoBlogSearchService kakaoBlogSearchService;

    @MockBean
    private ApplicationEventPublisher eventPublisher;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        kakaoBlogSearchService = new KakaoBlogSearchService(eventPublisher, restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Nested
    class 카카오_블로그_검색_요청을_보낼_때 {

        @Test
        void 검색결과가_없으면_빈_검색결과와_빈_다음페이지URL이_반환된다() throws IOException {
            // given
            String query = "query-without-result";
            String sort = "recency";
            int page = 1;
            String expectedResult = FileReader.readJson("resultEmpty.json");
            willRequest(query, sort, page, expectedResult);
            willDoNothing().given(eventPublisher).publishEvent(any(IncreaseBlogSearchCountEvent.class));
            // when
            BlogSearchResponse response = kakaoBlogSearchService.searchByQuery(BlogSearchServiceRequest.of(query, sort, page, false));
            // then
            assertAll(
                    () -> assertThat(response.getDocuments()).isEmpty(),
                    () -> assertThat(response.getNext()).isEmpty(),
                    () -> verify(eventPublisher).publishEvent(any(IncreaseBlogSearchCountEvent.class))
            );
        }

        @Test
        void 검색결과가_마지막페이지면_검색결과와_빈_다음페이지URL이_반환된다() throws IOException {
            // given
            String query = "springboot";
            String sort = "accuracy";
            int page = 50;
            String expectedResult = FileReader.readJson("resultHasNoNextPage.json");
            willRequest(query, sort, page, expectedResult);
            willDoNothing().given(eventPublisher).publishEvent(any(IncreaseBlogSearchCountEvent.class));
            // when
            BlogSearchResponse response = kakaoBlogSearchService.searchByQuery(BlogSearchServiceRequest.of(query, sort, page, page == MAX_PAGE));
            // then
            assertAll(
                    () -> assertThat(response.getDocuments()).hasSizeLessThanOrEqualTo(MAX_SIZE_OF_PAGE),
                    () -> assertThat(response.getNext()).isEmpty(),
                    () -> verify(eventPublisher).publishEvent(any(IncreaseBlogSearchCountEvent.class))
            );
        }

        @Test
        void 검색결과가_10개이하면_검색결과와_빈_다음페이지URL이_반환된다() throws IOException {
            // given
            String query = "springboot";
            String sort = "accuracy";
            int page = 1;
            String expectedResult = FileReader.readJson("resultHasLessThan10Contents.json");
            willRequest(query, sort, page, expectedResult);
            willDoNothing().given(eventPublisher).publishEvent(any(IncreaseBlogSearchCountEvent.class));
            // when
            BlogSearchResponse response = kakaoBlogSearchService.searchByQuery(BlogSearchServiceRequest.of(query, sort, page, page == MAX_PAGE));
            // then
            assertAll(
                    () -> assertThat(response.getDocuments()).hasSizeLessThanOrEqualTo(MAX_SIZE_OF_PAGE),
                    () -> assertThat(response.getNext()).isEmpty(),
                    () -> verify(eventPublisher).publishEvent(any(IncreaseBlogSearchCountEvent.class))
            );
        }

        @Test
        void 검색결과가_10개초과면_검색결과와_다음페이지URL이_반환된다() throws IOException {
            // given
            String query = "springboot";
            String sort = "accuracy";
            int page = 5;
            String expectedResult = FileReader.readJson("resultHasNextPage.json");
            String expectedNext = BLOG_SEARCH_URL + "?query=" + query + "&sort=" + sort + "&page=" + (page + 1);
            willRequest(query, sort, page, expectedResult);
            willDoNothing().given(eventPublisher).publishEvent(any(IncreaseBlogSearchCountEvent.class));
            // when
            BlogSearchResponse response = kakaoBlogSearchService.searchByQuery(BlogSearchServiceRequest.of(query, sort, page, page == MAX_PAGE));
            // then
            assertAll(
                    () -> assertThat(response.getDocuments()).hasSizeLessThanOrEqualTo(MAX_SIZE_OF_PAGE),
                    () -> assertThat(response.getNext()).isEqualTo(expectedNext),
                    () -> verify(eventPublisher).publishEvent(any(IncreaseBlogSearchCountEvent.class))
            );
        }

        private void willRequest(String query, String sort, int page, String expectedResult) {
            mockServer.expect(requestTo(KAKAO_BLOG_SEARCH_URL + "?query=" + query + "&sort=" + sort + "&page=" + page))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(expectedResult, MediaType.APPLICATION_JSON));
        }
    }
}
