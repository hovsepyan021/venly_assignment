package com.example.demo.requests;

import com.example.demo.validators.ValidAddWord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ValidAddWord
public class AddWordRequest {
    private String word1;
    private String word2;
    private String relation;

    public String getWord1() {
        return word1;
    }

    public String getWord2() {
        return word2;
    }

    public String getRelation() {
        return relation;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
