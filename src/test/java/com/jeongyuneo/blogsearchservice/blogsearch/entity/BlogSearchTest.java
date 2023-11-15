package com.jeongyuneo.blogsearchservice.blogsearch.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BlogSearchTest {

    @Test
    void 검색기록이_생성되면_count가_1로_초기화된다() {
        // when
        BlogSearch blogSearch = BlogSearch.from("키워드");
        // then
        assertThat(blogSearch.getCount()).isOne();
    }

    @Test
    void 검색횟수가_증가하면_count가_1만큼_증가한다() {
        // given
        BlogSearch blogSearch = BlogSearch.from("키워드");
        // when
        blogSearch.increase();
        // then
        assertThat(blogSearch.getCount()).isEqualTo(2);
    }
}
