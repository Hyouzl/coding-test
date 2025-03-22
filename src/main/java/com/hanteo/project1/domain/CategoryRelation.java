package com.hanteo.project1.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryRelation {
    private Long parentIdx;
    private Long childId;

}
