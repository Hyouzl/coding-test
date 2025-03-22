package com.hanteo.project1.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardCategoryRelation {
    private Board board;
    private Category category;
}
