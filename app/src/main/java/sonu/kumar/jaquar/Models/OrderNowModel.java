package sonu.kumar.jaquar.Models;

/**
 * Created by sonu on 4/9/18.
 */

public class OrderNowModel {
    int user_id,product_id,product_single_price,product_quantity,product_total_price;
    String product_image,product_name,product_code;

    public OrderNowModel(int user_id, int product_id, int product_single_price, int product_quantity, int product_total_price, String product_image, String product_name,String product_code) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.product_single_price = product_single_price;
        this.product_quantity = product_quantity;
        this.product_total_price = product_total_price;
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_code =product_code;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_single_price() {
        return product_single_price;
    }

    public void setProduct_single_price(int product_single_price) {
        this.product_single_price = product_single_price;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public int getProduct_total_price() {
        return product_total_price;
    }

    public void setProduct_total_price(int product_total_price) {
        this.product_total_price = product_total_price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
