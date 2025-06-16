package com.example.cookfluencerbackend.controller;

import com.example.cookfluencerbackend.service.FirebaseSearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private final FirebaseSearchService searchService;

    @Value("${firebase.collection}")
    private String collection;

    @Value("${firebase.search-field}")
    private String searchField;

    public SearchController(FirebaseSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public List<String> search(@RequestParam("q") String query) throws Exception {
        return searchService.search(collection, searchField, query);
    }
}
