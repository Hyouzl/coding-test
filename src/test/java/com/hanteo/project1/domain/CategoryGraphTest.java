package com.hanteo.project1.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CategoryGraphTest {

    private CategoryGraph sut = new CategoryGraph();

    @Test
    @DisplayName("전체 카테고리 분류별 조회")
    void test_getCategoryTreeAsJson() {

        // Given
        sut.addBoard(1L, "엑소 공지사항");
        sut.addBoard(2L, "첸 게시판");
        sut.addBoard(3L, "백현 게시판");
        sut.addBoard(4L, "시우민 게시판");

        sut.addBoard(5L, "방탄 공지사항");
        sut.addBoard(7L, "뷔 게시판");

        sut.addBoard(8L, "블랙핑크 공지사항");
        sut.addBoard(9L, "로제 게시판");

        sut.addCategory(1L, "남자", null);
        sut.addCategory(2L, "엑소", 1L);

        // 최하위 카테고리
        sut.addCategory(3L, "공지사항", 2L);
        sut.addCategoryBoardRelation(3L, 1L); // 엑소 공지사항 게시판
        sut.addCategory(4L, "첸", 2L);
        sut.addCategoryBoardRelation(4L, 2L); // 첸 게시판
        sut.addCategory(5L, "백현", 2L);
        sut.addCategoryBoardRelation(5L, 3L); // 백현 게시판
        sut.addCategory(6L, "시우민", 2L);
        sut.addCategoryBoardRelation(6L, 4L); // 시우민 게시판


        sut.addCategory(7L, "방탄소년단", 1L);
        // 최하위 카테고리
        sut.addCategory(8L, "공지사항", 7L);
        sut.addCategoryBoardRelation(2L, 5L); // 방탄 공지 게시판
        sut.addCategory(9L, "익명 게시판", 2L);
        sut.addCategoryBoardRelation(9L, 0L); //익명 게시판
        sut.addCategory(10L, "뷔", 2L);
        sut.addCategoryBoardRelation(10L, 7L); // 뷔 게시판


        sut.addCategory(11L, "여자", null);
        sut.addCategory(12L, "블랙핑크", 11L);
        // 최하위 카테고리
        sut.addCategory(13L, "공지사항", 12L);
        sut.addCategoryBoardRelation(13L, 8L); // 블핑 공지 게시판
        sut.addCategory(14L, "익명 게시판", 12L);
        sut.addCategoryBoardRelation(14L, 0L); // 익명 게시판
        sut.addCategory(15L, "로제", 12L);
        sut.addCategoryBoardRelation(15L, 9L); // 로제 게시판

        // When
        String json = sut.getCategoryTreeAsJson();
        // Then
        assertNotNull(json);
        System.out.println("카테고리 트리 JSON:\n" + json);
    }
    @Test
    @DisplayName("카테고리 이름으로 검색 조회 - 공지사항 검색 조회")
    void test_getCategoryTreeByName_카테고리() {

        // Given
        sut.addBoard(1L, "엑소 공지사항");
        sut.addBoard(2L, "첸 게시판");
        sut.addBoard(3L, "백현 게시판");
        sut.addBoard(4L, "시우민 게시판");

        sut.addBoard(5L, "방탄 공지사항");
        sut.addBoard(7L, "뷔 게시판");

        sut.addBoard(8L, "블랙핑크 공지사항");
        sut.addBoard(9L, "로제 게시판");

        sut.addCategory(1L, "남자", null);
        sut.addCategory(2L, "엑소", 1L);

        // 최하위 카테고리
        sut.addCategory(3L, "공지사항", 2L);
        sut.addCategoryBoardRelation(3L, 1L); // 엑소 공지사항 게시판
        sut.addCategory(4L, "첸", 2L);
        sut.addCategoryBoardRelation(4L, 2L); // 첸 게시판
        sut.addCategory(5L, "백현", 2L);
        sut.addCategoryBoardRelation(5L, 3L); // 백현 게시판
        sut.addCategory(6L, "시우민", 2L);
        sut.addCategoryBoardRelation(6L, 4L); // 시우민 게시판


        sut.addCategory(7L, "방탄소년단", 1L);
        // 최하위 카테고리
        sut.addCategory(8L, "공지사항", 7L);
        sut.addCategoryBoardRelation(2L, 5L); // 방탄 공지 게시판
        sut.addCategory(9L, "익명 게시판", 2L);
        sut.addCategoryBoardRelation(9L, 0L); // 익명 게시판
        sut.addCategory(10L, "뷔", 2L);
        sut.addCategoryBoardRelation(10L, 7L); // 뷔 게시판


        sut.addCategory(11L, "여자", null);
        sut.addCategory(12L, "블랙핑크", 11L);
        // 최하위 카테고리
        sut.addCategory(13L, "공지사항", 12L);
        sut.addCategoryBoardRelation(13L, 8L); // 블핑 공지 게시판
        sut.addCategory(14L, "익명 게시판", 12L);
        sut.addCategoryBoardRelation(14L, 0L); // 익명 게시판
        sut.addCategory(15L, "로제", 12L);
        sut.addCategoryBoardRelation(15L, 9L); // 로제 게시판

        // When
        List<Category> categories = sut.getCategoryTreeByName("공지사항");

        // Then
        assertEquals(3, categories.size());
        assertEquals("공지사항", categories.get(0).getName());

        System.out.println("이름이 '공지사항'인 카테고리 목록: " + sut.getCategoryByNameAsJson("공지사항"));
    }


    @Test
    @DisplayName("익명게시판 검색 조회")
    void test_getCategoryTreeByName_익명() {

        // Given
        sut.addBoard(1L, "엑소 공지사항");
        sut.addBoard(2L, "첸 게시판");
        sut.addBoard(3L, "백현 게시판");
        sut.addBoard(4L, "시우민 게시판");

        sut.addBoard(5L, "방탄 공지사항");
        sut.addBoard(7L, "뷔 게시판");

        sut.addBoard(8L, "블랙핑크 공지사항");
        sut.addBoard(9L, "로제 게시판");

        sut.addCategory(1L, "남자", null);
        sut.addCategory(2L, "엑소", 1L);

        // 최하위 카테고리
        sut.addCategory(3L, "공지사항", 2L);
        sut.addCategoryBoardRelation(3L, 1L); // 엑소 공지사항 게시판
        sut.addCategory(4L, "첸", 2L);
        sut.addCategoryBoardRelation(4L, 2L); // 첸 게시판
        sut.addCategory(5L, "백현", 2L);
        sut.addCategoryBoardRelation(5L, 3L); // 백현 게시판
        sut.addCategory(6L, "시우민", 2L);
        sut.addCategoryBoardRelation(6L, 4L); // 시우민 게시판


        sut.addCategory(7L, "방탄소년단", 1L);
        // 최하위 카테고리
        sut.addCategory(8L, "공지사항", 7L);
        sut.addCategoryBoardRelation(2L, 5L); // 방탄 공지 게시판
        sut.addCategory(9L, "익명 게시판", 7L);
        sut.addCategoryBoardRelation(9L, 0L); //익명 게시판
        sut.addCategory(10L, "뷔", 2L);
        sut.addCategoryBoardRelation(10L, 7L); // 뷔 게시판


        sut.addCategory(11L, "여자", null);
        sut.addCategory(12L, "블랙핑크", 11L);
        // 최하위 카테고리
        sut.addCategory(13L, "공지사항", 12L);
        sut.addCategoryBoardRelation(13L, 8L); // 블핑 공지 게시판
        sut.addCategory(14L, "익명 게시판", 12L);
        sut.addCategoryBoardRelation(14L, 0L); // 익명 게시판
        sut.addCategory(15L, "로제", 12L);
        sut.addCategoryBoardRelation(15L, 9L); // 로제 게시판

        // When
        List<Category> categories = sut.getCategoryTreeByName("익명 게시판");

        // Then
        assertEquals(2, categories.size());
        for (Category category : categories) {
            assertEquals(0L, category.getBoard().getId());
            assertEquals("익명 게시판", category.getName());
        }

        System.out.println("이름이 '익명 게시판'인 카테고리 목록: " + sut.getCategoryByNameAsJson("익명 게시판"));

    }


    @Test
    @DisplayName("블랙핑크 검색 조회")
    void test_getCategoryTreeByName_블랙핑크() throws JsonProcessingException {

        // Given
        sut.addBoard(1L, "엑소 공지사항");
        sut.addBoard(2L, "첸 게시판");
        sut.addBoard(3L, "백현 게시판");
        sut.addBoard(4L, "시우민 게시판");

        sut.addBoard(5L, "방탄 공지사항");
        sut.addBoard(7L, "뷔 게시판");

        sut.addBoard(8L, "블랙핑크 공지사항");
        sut.addBoard(9L, "로제 게시판");

        sut.addCategory(1L, "남자", null);
        sut.addCategory(2L, "엑소", 1L);

        // 최하위 카테고리
        sut.addCategory(3L, "공지사항", 2L);
        sut.addCategoryBoardRelation(3L, 1L); // 엑소 공지사항 게시판
        sut.addCategory(4L, "첸", 2L);
        sut.addCategoryBoardRelation(4L, 2L); // 첸 게시판
        sut.addCategory(5L, "백현", 2L);
        sut.addCategoryBoardRelation(5L, 3L); // 백현 게시판
        sut.addCategory(6L, "시우민", 2L);
        sut.addCategoryBoardRelation(6L, 4L); // 시우민 게시판


        sut.addCategory(7L, "방탄소년단", 1L);
        // 최하위 카테고리
        sut.addCategory(8L, "공지사항", 7L);
        sut.addCategoryBoardRelation(2L, 5L); // 방탄 공지 게시판
        sut.addCategory(9L, "익명 게시판", 7L);
        sut.addCategoryBoardRelation(9L, 0L); //익명 게시판
        sut.addCategory(10L, "뷔", 2L);
        sut.addCategoryBoardRelation(10L, 7L); // 뷔 게시판


        sut.addCategory(11L, "여자", null);
        sut.addCategory(12L, "블랙핑크", 11L);
        // 최하위 카테고리
        sut.addCategory(13L, "공지사항", 12L);
        sut.addCategoryBoardRelation(13L, 8L); // 블핑 공지 게시판
        sut.addCategory(14L, "익명 게시판", 12L);
        sut.addCategoryBoardRelation(14L, 0L); // 익명 게시판
        sut.addCategory(15L, "로제", 12L);
        sut.addCategoryBoardRelation(15L, 9L); // 로제 게시판

        // When
        List<Category> categories = sut.getCategoryTreeByName("블랙핑크");

        // Then
        assertEquals(1, categories.size());
        assertEquals(12L, categories.get(0).getId());
        assertEquals("블랙핑크", categories.get(0).getName());


        System.out.println("이름이 '블랙핑크'인 카테고리 목록: " + sut.getCategoryByNameAsJson("블랙핑크"));

    }

    @Test
    @DisplayName("방탄소년단 카테고리 아이디 검색 조회")
    void test_getCategoryTreeById_방탄()  {
        // Given
        sut.addBoard(1L, "엑소 공지사항");
        sut.addBoard(2L, "첸 게시판");
        sut.addBoard(3L, "백현 게시판");
        sut.addBoard(4L, "시우민 게시판");

        sut.addBoard(5L, "방탄 공지사항");
        sut.addBoard(7L, "뷔 게시판");

        sut.addBoard(8L, "블랙핑크 공지사항");
        sut.addBoard(9L, "로제 게시판");

        sut.addCategory(1L, "남자", null);
        sut.addCategory(2L, "엑소", 1L);

        // 최하위 카테고리
        sut.addCategory(3L, "공지사항", 2L);
        sut.addCategoryBoardRelation(3L, 1L); // 엑소 공지사항 게시판
        sut.addCategory(4L, "첸", 2L);
        sut.addCategoryBoardRelation(4L, 2L); // 첸 게시판
        sut.addCategory(5L, "백현", 2L);
        sut.addCategoryBoardRelation(5L, 3L); // 백현 게시판
        sut.addCategory(6L, "시우민", 2L);
        sut.addCategoryBoardRelation(6L, 4L); // 시우민 게시판


        sut.addCategory(7L, "방탄소년단", 1L);
        // 최하위 카테고리
        sut.addCategory(8L, "공지사항", 7L);
        sut.addCategoryBoardRelation(8L, 5L); // 방탄 공지 게시판
        sut.addCategory(9L, "익명 게시판", 7L);
        sut.addCategoryBoardRelation(9L, 0L); //익명 게시판
        sut.addCategory(10L, "뷔", 7L);
        sut.addCategoryBoardRelation(10L, 7L); // 뷔 게시판


        sut.addCategory(11L, "여자", null);
        sut.addCategory(12L, "블랙핑크", 11L);
        // 최하위 카테고리
        sut.addCategory(13L, "공지사항", 12L);
        sut.addCategoryBoardRelation(13L, 8L); // 블핑 공지 게시판
        sut.addCategory(14L, "익명 게시판", 12L);
        sut.addCategoryBoardRelation(14L, 0L); // 익명 게시판
        sut.addCategory(15L, "로제", 12L);
        sut.addCategoryBoardRelation(15L, 9L); // 로제 게시판

        // When
       Category category = sut.getCategoryTreeById(7L);

       assertEquals(7L, category.getId());
       assertEquals("방탄소년단", category.getName());
       System.out.println("방탄소년단 ID의 검색 시 카테고리 목록: " + sut.getCategoryByIdAsJson(7L));

    }



}