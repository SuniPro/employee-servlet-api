package com.taekang.employeeservletapi.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
public class PaginationResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int number; // 현재 페이지
    private int size;   // 페이지 사이즈
    private boolean first;
    private boolean last;
    private boolean empty;
    private Sort sort;
    private PageableInfo pageable;

    @Getter
    @Setter
    public static class PageableInfo {
        private int pageNumber;
        private int pageSize;
        private long offset;
        private boolean paged;
        private boolean unpaged;
        private Sort sort;

        // getters/setters
    }

    @Getter
    @Setter
    public static class Sort {
        private boolean sorted;
        private boolean unsorted;
        private boolean empty;
    }

    // ✅ 정적 팩토리 메서드
    public static <T> PaginationResponse<T> from(Page<T> page) {
        if (page == null) return new PaginationResponse<>();
        PaginationResponse<T> response = new PaginationResponse<>();
        response.content = page.getContent();
        response.totalPages = page.getTotalPages();
        response.totalElements = page.getTotalElements();
        response.number = page.getNumber();
        response.size = page.getSize();
        response.first = page.isFirst();
        response.last = page.isLast();
        response.empty = page.isEmpty();

        // Sort 정보
        Sort sort = new Sort();
        sort.sorted = page.getSort().isSorted();
        sort.unsorted = page.getSort().isUnsorted();
        sort.empty = page.getSort().isEmpty();
        response.sort = sort;

        // Pageable 정보
        Pageable pageable = page.getPageable();
        PageableInfo pageableInfo = new PageableInfo();
        pageableInfo.pageNumber = pageable.getPageNumber();
        pageableInfo.pageSize = pageable.getPageSize();
        pageableInfo.offset = pageable.getOffset();
        pageableInfo.paged = pageable.isPaged();
        pageableInfo.unpaged = pageable.isUnpaged();

        Sort innerSort = new Sort();
        innerSort.sorted = pageable.getSort().isSorted();
        innerSort.unsorted = pageable.getSort().isUnsorted();
        innerSort.empty = pageable.getSort().isEmpty();
        pageableInfo.sort = innerSort;

        response.pageable = pageableInfo;
        return response;
    }
}
