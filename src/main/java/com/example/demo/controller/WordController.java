package com.example.demo.controller;

import com.example.demo.models.WordRelation;
import com.example.demo.models.WordRelationInversed;
import com.example.demo.repositories.WordRelationRepository;
import com.example.demo.requests.AddWordRequest;
import com.example.demo.validators.ValidAddWord;
import com.example.demo.validators.ValidationErrorException;
import com.example.demo.validators.ValidationErrorResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/words")
public class WordController {
    private final WordRelationRepository wordRepository;

    public WordController(WordRelationRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

//    @GetMapping("/{id}")
//    public WordRelation getWordById(@PathVariable Long id) {
//        return wordRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Word not found with id: " + id));
//    }

    @PostMapping("/add")
    public ResponseEntity<String> addWord(@RequestBody @Valid AddWordRequest wordRequest) {
        WordRelation wordRelation = new WordRelation();
        wordRelation.setWord(formatWord(wordRequest.getWord1()));
        wordRelation.setRelatedWord(formatWord(wordRequest.getWord2()));
        wordRelation.setRelation(wordRequest.getRelation());
        failIfRelationExists(wordRelation);
        wordRepository.save(wordRelation);

        return new ResponseEntity<>("Word added successfully", HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<WordRelation> getAllWords(@RequestParam(name = "relation", required = false) String relation) {
        if(Strings.isNotBlank(relation)) {
            return wordRepository.findByRelation(relation);
        } else {
            return wordRepository.findAll();
        }
    }

    @GetMapping("/all/inverse")
    public List<WordRelationInversed> getInverseWords() {
        List<WordRelation> wordRelations = wordRepository.findAll();
        List<WordRelationInversed> result = new ArrayList<>();
        wordRelations.forEach(wordRelation -> {
            result.add(new WordRelationInversed(wordRelation.getId(), wordRelation.getWord(), wordRelation.getRelatedWord(), wordRelation.getRelation(), false));
        });
        wordRelations.forEach(wordRelation -> {
            result.add(new WordRelationInversed(wordRelation.getId(), wordRelation.getRelatedWord(), wordRelation.getWord(), wordRelation.getRelation(), true));
        });
        return result;
    }

    private void failIfRelationExists(WordRelation wordRelation) {
        if(wordRepository.wordRelationExists(wordRelation.getWord(), wordRelation.getRelatedWord(), wordRelation.getRelation())) {
            throw new ValidationErrorException(List.of(new ValidationErrorResponse("relation/word1/word2", "Relation already exists")));
        }
        if(wordRepository.wordRelationExists(wordRelation.getRelatedWord(), wordRelation.getWord(), wordRelation.getRelation())) {
            throw new ValidationErrorException(List.of(new ValidationErrorResponse("relation/word1/word2", "Inverse relation already exists")));
        }
    }

    private String formatWord(String word) {
        return word.toLowerCase().trim();
    }
}