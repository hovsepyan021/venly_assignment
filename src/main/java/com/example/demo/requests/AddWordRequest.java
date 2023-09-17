package com.example.demo.requests;

import com.example.demo.validators.ValidAddWord;

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
}
