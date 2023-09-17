package com.example.demo.repositories;

import com.example.demo.models.WordRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRelationRepository extends JpaRepository<WordRelation, Long> {

    @Query("SELECT w FROM WordRelation w WHERE w.relation = :relation")
    List<WordRelation> findByRelation(@Param("relation") String relation);

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END " +
            "FROM WordRelation w " +
            "WHERE w.word = :word1 AND w.relatedWord = :word2 AND w.relation = :relation")
    boolean wordRelationExists(
            @Param("word1") String word1,
            @Param("word2") String word2,
            @Param("relation") String relation
    );
}

