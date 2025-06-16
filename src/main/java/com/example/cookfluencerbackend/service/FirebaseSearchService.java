package com.example.cookfluencerbackend.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple search service that fetches documents from Firestore and performs a fuzzy
 * match against the requested keyword. This is not as powerful as Algolia but gives
 * basic similarity search functionality without external services.
 */

@Service
public class FirebaseSearchService {

    public List<String> search(String collection, String field, String keyword) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection(collection)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> doc.getString(field))
                .filter(value -> value != null && fuzzyMatch(value, keyword))
                .collect(Collectors.toList());
    }

    /**
     * Checks if the given text matches the keyword using substring or a simple
     * Levenshtein distance check for small typos.
     */
    private boolean fuzzyMatch(String text, String keyword) {
        text = text.toLowerCase();
        keyword = keyword.toLowerCase();
        if (text.contains(keyword)) {
            return true;
        }
        int len = keyword.length();
        for (int i = 0; i <= text.length() - len; i++) {
            String sub = text.substring(i, i + len);
            if (levenshteinDistance(sub, keyword) <= 1) {
                return true;
            }
        }
        return false;
    }

    // Classic dynamic-programming implementation of Levenshtein distance.
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[s1.length()][s2.length()];
    }
}
