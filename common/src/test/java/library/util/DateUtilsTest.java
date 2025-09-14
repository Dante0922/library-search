package library.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @DisplayName("문자열을 LocalDate로 변환한다.")
    @Test
    void parse() throws Exception{
        //given
        String date = "20250810";

        //when
        LocalDate result = DateUtils.parseYYYYMMDD(date);

        //then
        assertEquals(result, LocalDate.of(2025,8,10));
    }

}