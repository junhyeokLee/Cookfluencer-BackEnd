package com.example.cookfluencerbackend.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                .filter(value -> value != null && value.toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
