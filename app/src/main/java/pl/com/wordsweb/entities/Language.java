package pl.com.wordsweb.entities;

import java.io.Serializable;

/**
 * Created by wewe on 13.04.16.
 */
public class Language implements Serializable {

    private int id;

    private String name;

    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Language(){

    }
    public Language(String name, int id){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}