package pl.com.wordsweb.entities.quizzes;

import java.io.Serializable;

import pl.com.wordsweb.entities.Language;

/**
 * Created by wewe on 12.11.16.
 */
public class Expression implements Serializable {
    private int id;
    private int knowledgeLevel;
    private Language language;
    private String phrase;
    private String example;

    public Expression(int id, int knowledgeLevel, Language language, String phrase, String example) {
        this.id = id;
        this.knowledgeLevel = knowledgeLevel;
        this.language = language;
        this.phrase = phrase;
        this.example = example;
    }

    public int getId() {
        return id;
    }

    public int getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public Language getLanguage() {
        return language;
    }

    public String getPhrase() {
        return phrase;
    }

    public String getExample() {
        return example;
    }
}
