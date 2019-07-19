package model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private String password;
    private Map<Product, Integer> productsFromCart;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.productsFromCart = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addAndCountProduct(Product productToAdd) {
        Integer count = productsFromCart.get(productToAdd);
        if (count == null) {
            count = 1;
        } else {
            count += 1;
        }
        productsFromCart.put(productToAdd, count);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<Product, Integer> getProductsFromCart() {
        return productsFromCart;
    }

    public void setProductsFromCart(Map<Product, Integer> productsFromCart) {
        this.productsFromCart = productsFromCart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        return productsFromCart != null ? productsFromCart.equals(user.productsFromCart) : user.productsFromCart == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (productsFromCart != null ? productsFromCart.hashCode() : 0);
        return result;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Product product : getProductsFromCart().keySet()) {
            sb.append(product + " " + "quantity: " + getProductsFromCart().get(product));
        }
        return sb.toString();
    }
}
