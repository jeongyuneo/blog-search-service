package com.jeongyuneo.blogsearchservice.blogsearch.service;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchRankingResponse;
import com.jeongyuneo.blogsearchservice.blogsearch.entity.BlogSearch;
import com.jeongyuneo.blogsearchservice.blogsearch.repository.BlogSearchRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("검색 랭킹 서비스 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BlogSearchRankingServiceTest {

    private static final int INITIAL_BLOG_SEARCH_COUNT = 1;
    private static final int MAX_BLOG_SEARCH_RANKING_COUNT = 10;

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

        @Test
        void 저장된_검색기록이_있으면_검색횟수가_증가된다() {
            // given
            String keyword = "키워드";
            blogSearchRepository.save(BlogSearch.from(keyword));
            // when
            blogSearchRankingService.increaseSearchCount(keyword);
            // then
            assertThat(
                    blogSearchRepository.findByKeyword(keyword)
                            .orElseThrow()
                            .getCount())
                    .isEqualTo(2);
        }
    }

    @Nested
    class 인기_검색어_목록_조회할_때 {

        @Test
        void 검색기록이_없으면_빈_리스트를_반환한다() {
            // when
            BlogSearchRankingResponse searchRanking = blogSearchRankingService.getSearchRanking();
            // then
            assertThat(searchRanking.getBlogSearchRankings()).isEmpty();
        }

        @Test
        void 검색기록이_10개이하면_모든_검색기록을_반환한다() {
            // given
            List<BlogSearch> blogSearches = List.of(
                    BlogSearch.from("키워드1"),
                    BlogSearch.from("키워드2"),
                    BlogSearch.from("키워드3")
            );
            blogSearchRepository.saveAll(blogSearches);
            // when
            BlogSearchRankingResponse searchRanking = blogSearchRankingService.getSearchRanking();
            // then
            assertThat(searchRanking.getBlogSearchRankings()).hasSizeLessThan(MAX_BLOG_SEARCH_RANKING_COUNT);
        }

        @Test
        void 검색기록이_10개초과면_검색기록을_10개만_반환한다() {
            // given
            List<BlogSearch> blogSearches = List.of(
                    BlogSearch.from("키워드1"), BlogSearch.from("키워드2"), BlogSearch.from("키워드3"),
                    BlogSearch.from("키워드4"), BlogSearch.from("키워드5"), BlogSearch.from("키워드6"),
                    BlogSearch.from("키워드7"), BlogSearch.from("키워드8"), BlogSearch.from("키워드9"),
                    BlogSearch.from("키워드10"), BlogSearch.from("키워드11"), BlogSearch.from("키워드12")
            );
            blogSearchRepository.saveAll(blogSearches);
            // when
            BlogSearchRankingResponse searchRanking = blogSearchRankingService.getSearchRanking();
            // then
            assertThat(searchRanking.getBlogSearchRankings()).hasSize(MAX_BLOG_SEARCH_RANKING_COUNT);
        }
    }
}
