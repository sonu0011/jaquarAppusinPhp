package sonu.kumar.jaquar.Models;

/**
 * Created by sonu on 2/9/18.
 */

public class ProductsModel {
    String product_title,product_price,product_code,product_image;
    int product_id,cat_id,subcat_id;

    public ProductsModel(String product_title, String product_price, String product_code, String product_image, int product_id,int cat_id,int
                         subcat_id) {
        this.product_title = product_title;
        this.product_price = product_price;
        this.product_code = product_code;
        this.product_image = product_image;
        this.product_id = product_id;
        this.cat_id =cat_id;
        this.subcat_id =subcat_id;
    }

    public ProductsModel() {
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getSubcat_id() {
        return subcat_id;
    }

    public void setSubcat_id(int subcat_id) {
        this.subcat_id = subcat_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}
