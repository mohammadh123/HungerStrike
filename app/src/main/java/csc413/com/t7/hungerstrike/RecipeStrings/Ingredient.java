package csc413.com.t7.hungerstrike.RecipeStrings;
/**
 * Created by Mardan Anwar on 10/20/2015.
 */
public class Ingredient
{
    private String name;
    private int preferred = 1;

    public Ingredient(){

    }

    public Ingredient(String name, int preferred)
    {
        this.name = name;
        this.preferred = preferred;
    }

    public void setName(String name){
        this.name = name.toLowerCase().trim();
    }

    public void setPreferred(int preferred){
        this.preferred = preferred;
    }

    /** Intentionally commented out, in case more options are added later */
//    public void setPreferred(boolean preferred1){
//        if ( preferred1 ) {
//            this.preferred = 1;
//        } else {
//            this.preferred = 0;
//        }
//    }

    public String getName(){
        return this.name;
    }

    public int getPreferred(){
        return this.preferred;
    }

    public boolean isPreferred(){
        return this.getPreferred() == 1;
    }

    public String toString(){
        return this.name;
    }

}
