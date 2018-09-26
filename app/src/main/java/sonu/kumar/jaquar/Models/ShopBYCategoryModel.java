package sonu.kumar.jaquar.Models;

/**
 * Created by sonu on 2/9/18.
 */

public class ShopBYCategoryModel {
        int cat_id;
        String cat_name,image;

    public ShopBYCategoryModel(int cat_id, String cat_name, String image) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.image = image;
    }

    public ShopBYCategoryModel() {
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
