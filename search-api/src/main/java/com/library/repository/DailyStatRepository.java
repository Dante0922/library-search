package com.library.repository;

import com.library.controller.response.StatResponse;
import com.library.entity.DailyStat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyStatRepository extends JpaRepository<DailyStat, Long> {
    long countByQueryAndEventDateTimeBetween(String query, LocalDateTime start, LocalDateTime end);


    // TODO: DB에서 StatResponse를 쓰는 게 부적절할 수 있음. 내부 DTO로 변환 필요
    @Query("SELECT new com.library.controller.response.StatResponse(ds.query, count(ds.id))" +
        "FROM DailyStat ds " +
        "GROUP BY ds.query ORDER BY count(ds.query) DESC")
    List<StatResponse> findTopQuery(Pageable pageable);
}
