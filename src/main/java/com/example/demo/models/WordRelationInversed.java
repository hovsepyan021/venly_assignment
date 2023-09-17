package com.example.demo.models;


public class WordRelationInversed extends WordRelation {
    private boolean inversed;

    public WordRelationInversed(Long id, String word, String relation, String relatedWord, boolean inversed) {
        super(word, relation, relatedWord);
        setId(id);
        this.inversed = inversed;
    }

    public boolean isInversed() {
        return inversed;
    }

    public void setInversed(boolean inversed) {
        this.inversed = inversed;
    }
}

