package com.library.controller;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.service.BookQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookQueryService bookQueryService;

    @GetMapping
    public PageResult<SearchResponse> search(@RequestParam("query") String query,
                                             @RequestParam("page") int page,
                                             @RequestParam("size") int size) {
        return bookQueryService.search(query, page, size);
    }
}
