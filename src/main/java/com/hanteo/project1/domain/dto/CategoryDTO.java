package com.hanteo.project1.domain.dto;

import com.hanteo.project1.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId;
    private Long boardId;
    private boolean anonymous;
    private List<CategoryDTO> children;

}
