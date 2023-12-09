package com.jeongyuneo.blogsearchservice.blogsearch.service;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchRankingResponse;
import com.jeongyuneo.blogsearchservice.global.config.EmbeddedRedisConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("검색 랭킹 서비스 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import(EmbeddedRedisConfig.class)
@SpringBootTest
class BlogSearchRankingServiceTest {

    private static final String BOOK_SEARCH_RANKING_KEY = "bookSearchRanking";
    private static final int INITIAL_BLOG_SEARCH_COUNT = 1;
    private static final int MAX_BLOG_SEARCH_RANKING_COUNT = 10;

    @Autowired
    private BlogSearchRankingService blogSearchRankingService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    ZSetOperations<String, String> zSetOperations;

    @BeforeEach
    void setUp() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    @AfterEach
    void clear() {
        zSetOperations.removeRange(BOOK_SEARCH_RANKING_KEY, 0, Long.MAX_VALUE);
    }

    @Nested
    class 검색_횟수_증가할_때 {

        @Nested
        class 저장된_검색기록이_없으면 {

            @Test
            void 검색기록이_생성된다() {
                // given
                String keyword = "키워드";
                // when
                blogSearchRankingService.increaseSearchCount(keyword);
                // then
                assertThat(zSetOperations.zCard(BOOK_SEARCH_RANKING_KEY)).isEqualTo(1);
            }

            @Test
            void 검색횟수가_초기값이다() {
                // given
                String keyword = "키워드";
                // when
                blogSearchRankingService.increaseSearchCount(keyword);
                // then
                assertThat(zSetOperations.score(BOOK_SEARCH_RANKING_KEY, keyword)).isEqualTo(1);
            }
        }

        @Nested
        class 저장된_검색기록이_있으면 {

            @Test
            void 검색기록이_추가되지_않는다() {
                // given
                String keyword = "키워드";
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, keyword, INITIAL_BLOG_SEARCH_COUNT);
                // when
                blogSearchRankingService.increaseSearchCount(keyword);
                // then
                assertThat(zSetOperations.zCard(BOOK_SEARCH_RANKING_KEY)).isEqualTo(1);
            }

            @Test
            void 검색횟수가_증가한다() {
                // given
                String keyword = "키워드";
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, keyword, INITIAL_BLOG_SEARCH_COUNT);
                // when
                blogSearchRankingService.increaseSearchCount(keyword);
                // then
                assertThat(zSetOperations.score(BOOK_SEARCH_RANKING_KEY, keyword)).isEqualTo(2);
            }
        }
    }

    @Nested
    class 인기_검색어_목록_조회할_때 {

        @Nested
        class 검색기록이_없으면 {

            @Test
            void 빈_리스트를_반환한다() {
                // when
                BlogSearchRankingResponse searchRanking = blogSearchRankingService.getSearchRanking();
                // then
                assertThat(searchRanking.getBlogSearchRankings()).isEmpty();
            }
        }

        @Nested
        class 검색기록이_10개이하면 {

            @Test
            void 모든_검색기록을_반환한다() {
                // given
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드1", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드2", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드3", INITIAL_BLOG_SEARCH_COUNT);
                // when
                BlogSearchRankingResponse searchRanking = blogSearchRankingService.getSearchRanking();
                // then
                assertThat(searchRanking.getBlogSearchRankings()).hasSizeLessThan(MAX_BLOG_SEARCH_RANKING_COUNT);
            }
        }

        @Nested
        class 검색기록이_10개초과면 {

            @Test
            void 검색기록을_10개만_반환한다() {
                // given
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드1", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드2", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드3", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드4", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드5", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드6", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드7", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드8", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드9", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드10", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드11", INITIAL_BLOG_SEARCH_COUNT);
                zSetOperations.add(BOOK_SEARCH_RANKING_KEY, "키워드12", INITIAL_BLOG_SEARCH_COUNT);
                // when
                BlogSearchRankingResponse searchRanking = blogSearchRankingService.getSearchRanking();
                // then
                assertThat(searchRanking.getBlogSearchRankings()).hasSize(MAX_BLOG_SEARCH_RANKING_COUNT);
            }
        }
    }
}
