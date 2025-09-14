package com.library.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DailyStatTest {

    @DisplayName("")
    @Test
    void create() throws Exception{
        //given
        String query = "HTTP";
        LocalDateTime eventTime = LocalDateTime.now();

        //when
        DailyStat result = new DailyStat(query, eventTime);

        //then

        assertEquals(result.getQuery(), query);
        assertEquals(result.getEventDateTime(), eventTime);
    }

}