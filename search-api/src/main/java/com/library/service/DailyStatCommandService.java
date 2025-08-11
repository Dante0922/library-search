package com.library.service;

import com.library.entity.DailyStat;
import com.library.repository.DailyStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class DailyStatCommandService {

    private final DailyStatRepository dailyStatRepository;

    @Transactional
    public void save(DailyStat dailyStat) {
        log.info("save daily stats: {}", dailyStat);
        dailyStatRepository.save(dailyStat);
    }
}
