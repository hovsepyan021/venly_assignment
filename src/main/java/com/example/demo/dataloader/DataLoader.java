package com.example.demo.dataloader;

import com.example.demo.models.WordRelation;
import com.example.demo.repositories.WordRelationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final WordRelationRepository wordRepository;

    public DataLoader(WordRelationRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public void run(String... args)  {
        wordRepository.save(new WordRelation("son", "antonym", "daughter"));
        wordRepository.save(new WordRelation("road", "synonym", "street"));
        wordRepository.save(new WordRelation("road", "related", "avenue"));
        wordRepository.save(new WordRelation("synonym", "related", "match"));
        wordRepository.save(new WordRelation("antonym", "antonym", "synonym"));
    }
}

