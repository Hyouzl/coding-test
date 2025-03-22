package com.hanteo.project1.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
public class Category {

    private Long id;
    private String name;
    private Long parentId;
    @JsonIgnore
    private Board board;
    private List<Category> children;

    public void addChild(Category child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }
    public void setBoard(Board board) {
        this.board = board;
    }

}
