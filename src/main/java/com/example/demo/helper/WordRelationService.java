package com.example.demo.helper;

import com.example.demo.models.WordRelation;
import com.example.demo.repositories.WordRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordRelationService {

    private final WordRelationRepository wordRelationRepository;

    @Autowired
    public WordRelationService(WordRelationRepository wordRelationRepository) {
        this.wordRelationRepository = wordRelationRepository;
    }

    public String findRelationPath(String startWord, String endWord) {
        Map<String, String> visited = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visitedWords = new HashSet<>(); // Track visited words to detect loops

        queue.offer(startWord);
        visited.put(startWord, null);

        while (!queue.isEmpty()) {
            String currentWord = queue.poll();
            if (currentWord.equals(endWord)) {
                return buildRelationPath(visited, endWord);
            }

            List<WordRelation> relations = wordRelationRepository.findByWord(currentWord);
            for (WordRelation relation : relations) {
                String relatedWord = relation.getRelatedWord().equals(currentWord) ? relation.getWord() : relation.getRelatedWord();
                if (!visited.containsKey(relatedWord)) {
                    visited.put(relatedWord, relation.getRelation());
                    queue.offer(relatedWord);
                } else if (!visitedWords.contains(relatedWord)) {
                    // Detect a loop and break out
                    visitedWords.add(relatedWord);
                }
            }
        }

        return "No relation path found";
    }

    private String buildRelationPath(Map<String, String> visited, String endWord) {
        StringBuilder path = new StringBuilder();
        String currentWord = endWord;
        String separator = "";

        Set<String> visitedWords = new HashSet<>(); // Track visited words to prevent loops

        while (visited.get(currentWord) != null) {
            if (visitedWords.contains(currentWord)) {
                // Detected a loop, break to avoid infinite loop
                path.insert(0, separator + currentWord + " --> (LOOP)");
                break;
            }

            visitedWords.add(currentWord);

            String relation = visited.get(currentWord);
            path.insert(0, separator + currentWord + " --> (" + relation + ")");
            separator = " --> ";
            currentWord = getKeyByValue(visited, relation);
        }

        return path.toString();
    }



    private String getKeyByValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
