package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.entity.DailyStat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BookApplicationService {
    private final BookQueryService bookQueryService;
    private final DailyStatCommandService dailyStatCommandService;

    public PageResult<SearchResponse> search(String query, int page, int size) {
        // 외부 API 호출 -> 통계 데이터 저장 -> API호출값 응답
        PageResult<SearchResponse> response = bookQueryService.search(query, page, size);
        DailyStat dailyStat = new DailyStat(query, LocalDateTime.now());
        dailyStatCommandService.save(dailyStat);
        return response;
    }
}
