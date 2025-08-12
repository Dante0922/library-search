package com.library.service;

import com.library.controller.response.StatResponse;
import com.library.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DailyStatQueryService {
    private static final int PAGE = 0;
    private static final int SIZE = 5;
    private final DailyStatRepository dailyStatRepository;

    public StatResponse findQueryCount(String query, LocalDate date) {
        long count = dailyStatRepository.countByQueryAndEventDateTimeBetween(query, date.atStartOfDay(), date.atTime(LocalTime.MAX));

        return new StatResponse(query, count);
    }

    public List<StatResponse> findTop5Query() {
        Pageable pageable = PageRequest.of(PAGE, SIZE);
        return dailyStatRepository.findTopQuery(pageable);
    }
}
