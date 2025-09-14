package com.library.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "검색결과")
public record SearchResponse(
    @Schema(description = "제목", example = "HTTP완벽가이드")
    String title,
    @Schema(description = "저자", example = "데이빗 고올리")
    String author,
    @Schema(description = "출판사", example = "인사이트")
    String publisher,
    @Schema(description = "출파일", example = "2015-01-01")
    LocalDate pubDate,
    @Schema(description = "ISBN", example = "999999123999")
    String isbn) {
}
