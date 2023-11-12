package com.jeongyuneo.blogsearchservice.blogsearch.dto.kakaoapi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Meta {

    private Integer total_count;
    private Integer pageable_count;
    private Boolean is_end;
}
