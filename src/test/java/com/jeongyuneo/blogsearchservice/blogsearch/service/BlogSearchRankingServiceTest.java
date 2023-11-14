package com.jeongyuneo.blogsearchservice.blogsearch.service;

import com.jeongyuneo.blogsearchservice.blogsearch.repository.BlogSearchRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("검색 랭킹 서비스 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class BlogSearchRankingServiceTest {

    private static final int INITIAL_BLOG_SEARCH_COUNT = 1;

    @Autowired
    private BlogSearchRankingService blogSearchRankingService;

    @Autowired
    private BlogSearchRepository blogSearchRepository;

    @Nested
    class 검색_횟수_증가할_때 {

        @Test
        void 저장된_검색기록이_없으면_검색기록이_생성된다() {
            // given
            String keyword = "키워드";
            // when
            blogSearchRankingService.increaseSearchCount(keyword);
            // then
            assertThat(
                    blogSearchRepository.findByKeyword(keyword)
                            .orElseThrow()
                            .getCount())
                    .isEqualTo(INITIAL_BLOG_SEARCH_COUNT);
        }
    }
}
