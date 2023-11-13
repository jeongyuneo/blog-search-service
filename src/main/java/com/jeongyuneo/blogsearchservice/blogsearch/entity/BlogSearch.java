package com.jeongyuneo.blogsearchservice.blogsearch.entity;

import com.jeongyuneo.blogsearchservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE blog_search SET is_deleted = true WHERE blog_search_id = ?")
@Where(clause = "deleted = false")
@AttributeOverride(name = "id", column = @Column(name = "blog_search_id"))
@Entity
public class BlogSearch extends BaseEntity {

    private static final int INITIAL_VALUE = 1;

    private String keyword;
    private int count;

    public static BlogSearch from(String keyword) {
        return new BlogSearch(keyword, INITIAL_VALUE);
    }

    public void increase() {
        count++;
    }
}
