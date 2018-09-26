package sonu.kumar.jaquar.Models;

/**
 * Created by sonu on 3/9/18.
 */

public class SubCategoryModel {
    int subcat_id,cat_id;
    String subcat_name,image;

    public SubCategoryModel(int subcat_id, int cat_id, String subcat_name, String image) {
        this.subcat_id = subcat_id;
        this.cat_id = cat_id;
        this.subcat_name = subcat_name;
        this.image = image;
    }

    public SubCategoryModel() {
    }

    public int getSubcat_id() {
        return subcat_id;
    }

    public void setSubcat_id(int subcat_id) {
        this.subcat_id = subcat_id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getSubcat_name() {
        return subcat_name;
    }

    public void setSubcat_name(String subcat_name) {
        this.subcat_name = subcat_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
