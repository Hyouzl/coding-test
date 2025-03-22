package com.hanteo.project1.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanteo.project1.exception.BoardException;
import com.hanteo.project1.exception.CategoryException;
import com.hanteo.project1.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryGraphTest {

    private CategoryGraph sut = new CategoryGraph();
    @BeforeEach
    void setUp() {
        sut = new CategoryGraph();

        // 게시판 추가
        sut.addBoard(1L, "엑소 공지사항");
        sut.addBoard(2L, "첸 게시판");
        sut.addBoard(3L, "백현 게시판");
        sut.addBoard(4L, "시우민 게시판");
        sut.addBoard(5L, "방탄 공지사항");
        sut.addBoard(7L, "뷔 게시판");
        sut.addBoard(8L, "블랙핑크 공지사항");
        sut.addBoard(9L, "로제 게시판");

        // 카테고리 추가
        sut.addCategory(1L, "남자", null);
        sut.addCategory(2L, "엑소", 1L);
        sut.addCategory(3L, "공지사항", 2L);
        sut.addCategory(4L, "첸", 2L);
        sut.addCategory(5L, "백현", 2L);
        sut.addCategory(6L, "시우민", 2L);

        sut.addCategory(7L, "방탄소년단", 1L);
        sut.addCategory(8L, "공지사항", 7L);
        sut.addCategory(9L, "익명 게시판", 7L);
        sut.addCategory(10L, "뷔", 7L);

        sut.addCategory(11L, "여자", null);
        sut.addCategory(12L, "블랙핑크", 11L);
        sut.addCategory(13L, "공지사항", 12L);
        sut.addCategory(14L, "익명 게시판", 12L);
        sut.addCategory(15L, "로제", 12L);

        // 카테고리-게시판 관계 추가
        sut.addCategoryBoardRelation(3L, 1L);  // 엑소 공지사항 게시판
        sut.addCategoryBoardRelation(4L, 2L);  // 첸 게시판
        sut.addCategoryBoardRelation(5L, 3L);  // 백현 게시판
        sut.addCategoryBoardRelation(6L, 4L);  // 시우민 게시판

        sut.addCategoryBoardRelation(8L, 5L);  // 방탄 공지 게시판
        sut.addCategoryBoardRelation(9L, 0L);  // 익명 게시판
        sut.addCategoryBoardRelation(10L, 7L); // 뷔 게시판

        sut.addCategoryBoardRelation(13L, 8L); // 블핑 공지 게시판
        sut.addCategoryBoardRelation(14L, 0L); // 익명 게시판
        sut.addCategoryBoardRelation(15L, 9L); // 로제 게시판
    }

    @Test
    @DisplayName("전체 카테고리 분류별 조회")
    void test_getCategoryTreeAsJson() {

        // When
        String json = sut.getCategoryTreeAsJson();
        // Then
        assertNotNull(json);
        System.out.println("카테고리 트리 JSON:\n" + json);
    }
    @Test
    @DisplayName("카테고리 이름으로 검색 조회 - 공지사항 검색 조회")
    void test_getCategoryTreeByName_카테고리() {

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

        // When
       Category category = sut.getCategoryTreeById(7L);

       assertEquals(7L, category.getId());
       assertEquals("방탄소년단", category.getName());
       System.out.println("방탄소년단 ID의 검색 시 카테고리 목록: " + sut.getCategoryByIdAsJson(7L));

    }


    @Test
    @DisplayName("부모 카테고리가 없는 경우 예외 처리")
    void addCategory_withoutParent_throwsException() {
        CategoryException exception = assertThrows(CategoryException.class, () ->
                sut.addCategory(1L, "남자", 99L)
        );
        assertEquals(ErrorCode.CATEGORY_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("중복된 카테고리를 추가할 때 예외 처리")
    void addDuplicateCategory_throwsException() {
        CategoryException exception = assertThrows(CategoryException.class, () ->
                sut.addCategory(1L, "남자", null)
        );
        assertEquals(ErrorCode.DUPLICATED_CATEGORY, exception.getErrorCode());
    }

    @Test
    @DisplayName("중복된 게시판 ID를 추가할 때 예외 처리")
    void addDuplicateBoard_throwsException() {
       BoardException exception = assertThrows(BoardException.class, () ->
                sut.addBoard(1L, "첸 게시판")
        );
        assertEquals(ErrorCode.DUPLICATED_BOARD, exception.getErrorCode());
    }

    @Test
    @DisplayName("존재하지 않는 게시판에 카테고리 추가할 때 예외 처리")
    void addCategoryToNonExistentBoard_throwsException() {
        CategoryException exception = assertThrows(CategoryException.class, () ->
                sut.addCategoryBoardRelation(1L, 99L)
        );
        assertEquals(ErrorCode.BOARD_NOT_FOUND, exception.getErrorCode());
    }
    @Test
    @DisplayName("존재하지 않는 카테고리에 게시판 추가할 때 예외 처리")
    void addBoardToNonExistentCategory_throwsException() {
        CategoryException exception = assertThrows(CategoryException.class, () ->
                sut.addCategoryBoardRelation(99L, 1L)
        );
        assertEquals(ErrorCode.CATEGORY_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("중복된 카테고리와 게시판 관계 추가 시 예외 처리")
    void addDuplicateCategoryBoardRelation_throwsException() {

        CategoryException exception = assertThrows(CategoryException.class, () ->
                sut.addCategoryBoardRelation(6L, 4L)  // 시우민 게시판
        );
        assertEquals(ErrorCode.DUPLICATED_CATEGORY, exception.getErrorCode());
    }


    @Test
    @DisplayName("존재하지 않는 카테고리 이름으로 조회 시 예외 처리")
    void getCategoryByName_notFound_throwsException() {
        CategoryException exception = assertThrows(CategoryException.class, () ->
                sut.getCategoryTreeByName("없는카테고리")
        );
        assertEquals(ErrorCode.CATEGORY_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    @DisplayName("존재하지 않는 카테고리 ID로 조회 시 예외 처리")
    void getCategoryById_notFound_throwsException() {
        CategoryException exception = assertThrows(CategoryException.class, () ->
                sut.getCategoryTreeById(99L)
        );
        assertEquals(ErrorCode.CATEGORY_NOT_FOUND, exception.getErrorCode());
    }

}