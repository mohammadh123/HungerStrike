package csc413.com.t7.hungerstrike.RecipeStrings;
/**
 * Created by Mardan Anwar and Anu on 10/30/2015.
 */
public class RecipeTie
{
    private String title;
    private String image_url;
    private String rId;
    private String apiname;
    private String[] ingredientarr;

    public String getApiname() {
        return apiname;
    }

    public void setApiname(String apiname) {
        this.apiname = apiname;
    }

    public String[] getIngredientarr() {
        return ingredientarr;
    }

    public void setIngredientarr(String[] ingredientarr) {
        this.ingredientarr = ingredientarr;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setRId(String rId) {
        this.rId = rId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public String getRId() {
        return this.rId;
    }






//    private String label;
//    private String image;
//    private String hits;
//
//    public String getHits() {
//        return hits;
//    }
//
//    public void setHits(String hits) {
//        this.hits = hits;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public void setLabel(String label) {
//        this.label = label;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }

//    public int[] getNUtilised_nMissing(List<Ingredient> ingredients) {
//        int[] n = new int[2];
//        int nmatch = 0;
//        String allIngredients = ingredientsToString();
//        Iterator<Ingredient> ing_iter = ingredients.iterator();
//        while (ing_iter.hasNext()) {
//            if (allIngredients.toLowerCase().trim().contains(ing_iter.next().getName())) {
//                nmatch += 1;
//            }
//        }
//        n[0] = nmatch;
//        n[1] = this.ingredients.size() - nmatch;
//        return n;

}


