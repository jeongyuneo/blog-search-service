package com.jeongyuneo.blogsearchservice.blogsearch.controller;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchResponse;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchServiceRequest;
import com.jeongyuneo.blogsearchservice.blogsearch.service.BlogSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/blogs/search")
public class BlogSearchController {

    private static final String SORT_PARAMETER = "accuracy|recency";
    private static final int MIN_PAGE = 1;
    private static final int MAX_PAGE = 50;

    private final BlogSearchService blogSearchService;

    @GetMapping
    public ResponseEntity<BlogSearchResponse> searchByQuery(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "accuracy") @Pattern(regexp = SORT_PARAMETER) String sort,
            @RequestParam(required = false, defaultValue = "1") @Min(MIN_PAGE) @Max(MAX_PAGE) int page
    ) {
        return ResponseEntity
                .ok()
                .body(blogSearchService.searchByQuery(BlogSearchServiceRequest.of(query, sort, page, page == MAX_PAGE)));
    }
}
