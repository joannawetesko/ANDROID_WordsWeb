package pl.com.wordsweb.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jwetesko on 12.04.16.
 */
public class LessonList implements Serializable {

    private Integer id;
    private String name;
    private Language firstLanguage;
    private Language secondLanguage;
    private ArrayList<Pair> pairs;
    private int size;

    public LessonList(String name, Language firstLanguage, Language secondLanguage, int size) {
        this.id = null;
        this.name = name;
        this.firstLanguage = firstLanguage;
        this.secondLanguage = secondLanguage;
        this.size = size;
    }

    public String getName() { return name; }
    public Language getFirstLanguage() { return firstLanguage; }
    public Language getSecondLanguage() { return secondLanguage; }
    public int getId() { return id; }
    public int getSize() { return size; }
    public ArrayList<Pair> getPairs() { return this.pairs; }

    public void setName(String name) { this.name = name; }
    public void setFirstLanguage(Language firstLanguage) {
        this.firstLanguage = firstLanguage;
    }
    public void setSecondLanguage(Language secondLanguage) {
        this.secondLanguage = secondLanguage;
    }
    public void setId(int id) { this.id = id; }
    public void setSize(int size) { this.size = size; }
    public void setPairs(ArrayList<Pair> pairs) { this.pairs = pairs; }
}
