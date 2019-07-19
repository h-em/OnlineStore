package utils;

import model.Product;
import model.Store;
import model.User;


public class Util {

    public static boolean isValidLine(String[] args) {
        if (args.length != 3) return false;
        return true;
    }

    public static boolean isUserLogged(User user) {
        if (user == null) return false;
        return true;
    }

    public static boolean areEnoughItemsInStore(Store store, Product product, int requiredQuantity){
        Integer currentQuantity = store.getProducts().get(product);
        if(currentQuantity <= requiredQuantity){
            System.out.println("There are not enough items! Current quantity is: " + currentQuantity);
            return false;
        }
        return true;
    }
}
