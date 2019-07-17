package model;

import services.LoginService;

import java.util.HashMap;
import java.util.Map;

public class Store {
    private String name;
    private String address;
    private Map<String, Integer> products;

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
        this.products = new HashMap<>();
    }


    public void addAndCountProduct(String productToAdd) {
        Integer count = products.get(productToAdd);
        if (count == null) {
            count = 1;
        } else {
            count = count + 1;
        }
        products.put(productToAdd, count);
    }


    public void buildProductList() {

        LoginService loginService = new LoginService();
        Map<Product, Integer> products = loginService.readAvailableProducts();
        if (products.isEmpty()) {
            System.out.println("No products in store!");
        } else {
            for (Product product : products.keySet()) {
                addAndCountProduct(product.getName());
            }
        }
    }


    public void addProduct(String nameOfProduct, Integer quantity) {
        products.put(nameOfProduct, quantity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Integer> products) {
        this.products = products;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String product : getProducts().keySet()) {
            sb.append("Name: " + product + ", " + "quantity: " + getProducts().get(product));
        }
        return sb.toString();
    }
}
