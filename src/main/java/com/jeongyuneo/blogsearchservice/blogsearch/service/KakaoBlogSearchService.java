package com.jeongyuneo.blogsearchservice.blogsearch.service;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchResponse;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchResponseElement;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchServiceRequest;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.kakaoapi.Document;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.kakaoapi.KakaoBlogSearchResponse;
import com.jeongyuneo.blogsearchservice.blogsearch.event.BlogSearchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class KakaoBlogSearchService implements BlogSearchService {

    private static final String KAKAO_BLOG_SEARCH_URL = "https://dapi.kakao.com/v2/search/blog";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String KAKAO_AUTHORIZATION_HEADER_PREFIX = "KakaoAK ";
    private static final String BLOG_SEARCH_URL = "http://localhost:8080/api/v1/blogs/search";
    private static final String QUERY_PARAMETER_NAME = "query";
    private static final String SORT_PARAMETER_NAME = "sort";
    private static final String PAGE_PARAMETER_NAME = "page";

    private final ApplicationEventPublisher eventPublisher;
    private final RestTemplate restTemplate;

    @Value("${kakao.api.key}")
    private String API_KEY;

    @Override
    @Transactional
    public BlogSearchResponse searchByQuery(BlogSearchServiceRequest request) {
        eventPublisher.publishEvent(BlogSearchEvent.from(request.getQuery()));
        KakaoBlogSearchResponse kakaoBlogSearchResponse = requestBookSearch(getRequestUrl(request), new HttpEntity<>(getHeaders()));
        List<Document> documents = Objects.requireNonNull(kakaoBlogSearchResponse).getDocuments();
        if (documents.isEmpty()) {
            return BlogSearchResponse.empty();
        }
        if (hasNoNext(request, kakaoBlogSearchResponse)) {
            return BlogSearchResponse.from(toBlogSearchResponseElement(documents));
        }
        return BlogSearchResponse.of(toBlogSearchResponseElement(documents), getNextUrl(request));
    }

    private String getRequestUrl(BlogSearchServiceRequest request) {
        return getUrl(KAKAO_BLOG_SEARCH_URL, request.getQuery(), request.getSort(), request.getPage());
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(AUTHORIZATION_HEADER, KAKAO_AUTHORIZATION_HEADER_PREFIX + API_KEY);
        return headers;
    }

    private KakaoBlogSearchResponse requestBookSearch(String requestUri, HttpEntity<HttpHeaders> httpEntity) {
        return restTemplate.exchange(requestUri, HttpMethod.GET, httpEntity, KakaoBlogSearchResponse.class)
                .getBody();
    }

    private boolean hasNoNext(BlogSearchServiceRequest request, KakaoBlogSearchResponse kakaoBlogSearchResponse) {
        return request.isMaxPage() || kakaoBlogSearchResponse.getMeta().getIs_end();
    }

    private String getNextUrl(BlogSearchServiceRequest request) {
        return getUrl(BLOG_SEARCH_URL, request.getQuery(), request.getSort(), request.getPage() + 1);
    }

    private String getUrl(String baseUrl, String query, String sort, int page) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam(QUERY_PARAMETER_NAME, query)
                .queryParam(SORT_PARAMETER_NAME, sort)
                .queryParam(PAGE_PARAMETER_NAME, page)
                .build()
                .toString();
    }

    private List<BlogSearchResponseElement> toBlogSearchResponseElement(List<Document> documents) {
        return documents.stream()
                .map(BlogSearchResponseElement::from)
                .collect(Collectors.toList());
    }
}
