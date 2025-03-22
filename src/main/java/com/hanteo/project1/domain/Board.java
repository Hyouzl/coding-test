package com.hanteo.project1.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class Board {
    private Long id;
    private String name;
    private boolean isAnonymous;
    private Category category;

    private static final Board anonymousInstance = Board.builder()
            .id(0L)
            .name("익명 게시판")
            .isAnonymous(true)
            .build();

    public void setCategory(Category category) {
        this.category = category;
    }

    public static Board getAnonymousInstance() {
        return anonymousInstance;
    }

}
