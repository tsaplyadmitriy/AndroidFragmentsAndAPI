package d.tsaplya.guidelead;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPreferences implements Serializable {

    //public Long id;
    public String categoryList;
    public Duration duration;

     private ArrayList<Category>  categoryListArray;

    UserPreferences(ArrayList<Category> categoryList,Duration duration){
        this.categoryListArray = categoryList;
        this.categoryList = categoryList.toString();;
        this.duration = duration;

    }

    public  UserPreferences(){

    }


    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ArrayList<Category> getCategoryList() {
        return categoryListArray;
    }

    public String getCategoryListString(){
        return this.categoryList;
    }
    public void setCategoryList(ArrayList<Category> categoryList) {
        this.categoryListArray = categoryList;
    }

    @Override
    public String toString(){
        return "catagories"+categoryList+" \n"+duration.toString();
    }

}
