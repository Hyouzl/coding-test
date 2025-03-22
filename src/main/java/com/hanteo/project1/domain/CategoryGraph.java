package com.hanteo.project1.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanteo.project1.domain.dto.CategoryDTO;
import com.hanteo.project1.exception.BoardException;
import com.hanteo.project1.exception.CategoryException;
import com.hanteo.project1.exception.ErrorCode;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class CategoryGraph {
    private final Map<Long, Board> boardMap = new HashMap<>();
    private final Map<Long, Category> categoryMap = new HashMap<>();

    // cache로 사용할 ConcurrentHashMap
    private final Map<Long, List<Category>> cachedTree = new ConcurrentHashMap<>();
    // 이름으로 검색 시 사용할 카테고리 캐시
    private final Map<String, List<Category>> cachedCategoryForNameSearch = new ConcurrentHashMap<>();
    // 식별자 검색 시 사용할 카테고리 캐시
    private final Map<Long, Category> cachedCategoryForIdSearch = new ConcurrentHashMap<>();


    // 관계를 관리하는 리스트
    private final Map<Long, Set<Long>> boardCategoryMap = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();


    // 게시판 추가
    public void addBoard(Long boardId, String name) {
        if (boardMap.get(boardId) != null) {
            throw new BoardException(ErrorCode.DUPLICATED_BOARD, "아이디를 확인해주세요.");
        }
        Board board = Board.builder()
                .id(boardId)
                .name(name)
                .build();
        boardMap.put(boardId, board);
    }

    // 카테고리 추가
    public void addCategory (Long id, String name, Long parentId) {
        if(parentId != null && !categoryMap.containsKey(parentId)) {
            throw new CategoryException(ErrorCode.CATEGORY_NOT_FOUND, "해당 상위 카테고리가 존재하지 않습니다.");
        }

        if (categoryMap.get(id) != null) {
            throw new CategoryException(ErrorCode.DUPLICATED_CATEGORY, "이미 존재하는 카테고리입니다.");
        }

        Category category = Category.builder()
                .id(id)
                .name(name)
                .parentId(parentId)
                .children(new ArrayList<>())
                .build();

        categoryMap.put(id, category);
        invalidateCache();
    }

    // 보드와 카테고리의 관계 추가 (다대다 관계)
    public void addCategoryBoardRelation(Long categoryId, Long boardId) {
        Board board;
        if (boardId == 0L) {
            board = Board.getAnonymousInstance();
        } else {
            board = boardMap.get(boardId);
            if (board == null) {
                throw new CategoryException(ErrorCode.BOARD_NOT_FOUND, "게시판을 찾을 수 없습니다: ID " + boardId);
            }
        }

        Category category = categoryMap.get(categoryId);
        if (category == null) {
            throw new CategoryException(ErrorCode.CATEGORY_NOT_FOUND, "카테고리를 찾을 수 없습니다: ID " + categoryId);
        }

        // 이미 존재하는 관계인지 확인 (중복 체크 개선)
        Set<Long> relatedCategories = boardCategoryMap.computeIfAbsent(boardId, k -> new HashSet<>());
        if (!relatedCategories.add(categoryId)) {
            System.out.println("이미 존재하는 관계: Board ID = " + boardId + ", Category ID = " + categoryId);
            throw new CategoryException(ErrorCode.DUPLICATED_CATEGORY, "중복된 카테고리 설정입니다.");
        }

        // 최하위 카테고리 저장
        board.setCategory(category);
        category.setBoard(board);

    }



    public void invalidateCache() {
        cachedTree.clear();
        cachedCategoryForNameSearch.clear();
        cachedCategoryForIdSearch.clear();
    }


    // 카테고리 조회 시 구조화
    public List<Category> getCategoryTree() {
        if (!cachedTree.isEmpty()) {
            return cachedTree.get(0L);
        }

        Map<Long, Category> categories = new HashMap<>(categoryMap);
        List<Category> categoryTree = new ArrayList<>();

        for (Category category : categories.values()) {
            if (category.getParentId() == null) {
                categoryTree.add(category);
            } else {
                Category parent = categories.get(category.getParentId());
                Category child = categories.get(category.getId());
                if (parent == null || child == null) {
                    throw new CategoryException(ErrorCode.CATEGORY_NOT_FOUND, "해당하는 상위 카테고리나 하위 카테고리가 존재하지 않습니다.");
                }
                parent.addChild(child);
            }
            cacheCategoryByName(category);
            cacheCategoryById(category);
        }

        cachedTree.put(0L, categoryTree);
        return categoryTree;
    }


    public List<Category> fetchCategoriesByName(String name) {
        List<Category> matchNameCategories = new ArrayList<>();
        for(Category category: categoryMap.values()) {
            if (category.getName().equals(name)) {
                matchNameCategories.add(category);
            }
        }
        return matchNameCategories;
    }


    // 이름별 카테고리 조회
    public List<Category> getCategoryTreeByName(String name) {
        if (cachedCategoryForNameSearch.containsKey(name)) {
            return cachedCategoryForNameSearch.get(name);
        }

        // 이름으로 검색하여 루트 카테고리들 조회
        List<Category> rootCategories = fetchCategoriesByName(name);

        // 결과가 없는 경우 예외 처리
        if (rootCategories.isEmpty()) {
            throw new CategoryException(ErrorCode.CATEGORY_NOT_FOUND, "존재하지 않는 카테고리입니다.");
        }

        // 이름 검색 시 트리 구조를 새로 생성하여 하위 카테고리까지 포함
        List<Category> completeResult = new ArrayList<>();
        for (Category root : rootCategories) {
            completeResult.add(constructCategoryTree(root));
        }

        // 캐싱 처리
        cachedCategoryForNameSearch.put(name, completeResult);
        return completeResult;
    }


    // 식별자 별 카테고리 조회
    public Category getCategoryTreeById(Long id) {
        if (cachedCategoryForIdSearch.containsKey(id)) {
            return cachedCategoryForIdSearch.get(id);
        }
        if (categoryMap.get(id) == null) {
            throw new CategoryException(ErrorCode.CATEGORY_NOT_FOUND, "카테고리를 찾을 수 없습니다.");
        }
        Category result = constructCategoryTree(categoryMap.get(id));

        // 캐싱 적용
        cachedCategoryForIdSearch.put(id, result);
        return result;
    }



    // 트리 구조를 새로 구성하여 반환
    private Category constructCategoryTree(Category root) {
        List<Category> children = categoryMap.values().stream()
                .filter(category -> root.getId().equals(category.getParentId()))
                .toList();

        for (Category child : children) {
            child = constructCategoryTree(child); // 재귀로 자식 노드도 트리화
            root.addChild(child);
        }

        return root;
    }

    // 캐시 추가 메서드
    private void cacheCategoryByName(Category category) {
        cachedCategoryForNameSearch.computeIfAbsent(category.getName(), k -> new ArrayList<>()).add(category);
    }
    private void cacheCategoryById(Category category) {
        cachedCategoryForIdSearch.put(category.getId(), category);
    }

    public String getDataAsJson(Object data) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new CategoryException(ErrorCode.JSON_PROCESSING_ERROR, "JSON 변환 중 오류 발생: " + e.getMessage());
        }
    }

    // 트리 구조 반환 - JSON 형식
    public String getCategoryTreeAsJson() {
        return getDataAsJson(getCategoryTree());
    }
    // 이름으로 카테고리 조회 (캐시 사용) - JSON 형식
    public String getCategoryByNameAsJson(String name) {
        return getDataAsJson(toDTOList(getCategoryTreeByName(name)));
    }
    // 이름으로 카테고리 조회 (캐시 사용) - JSON 형식
    public String getCategoryByIdAsJson(Long id) {
        return getDataAsJson(toDto(getCategoryTreeById(id)));
    }


    // DTO 변환
    public CategoryDTO toDto(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParentId())
                .boardId(category.getBoard() != null ? category.getBoard().getId() : null)
                .anonymous(category.getBoard() != null && category.getBoard().isAnonymous())
                .children(category.getChildren().stream().map(this::toDto).toList())
                .build();
    }

    public List<CategoryDTO> toDTOList(List<Category> categories) {
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map((category)->toDto(category))
                .toList();

        return categoryDTOS;
    }


}

