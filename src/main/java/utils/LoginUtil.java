package utils;

import model.Store;
import model.User;

import java.util.ArrayList;

public class LoginUtil {

    public static boolean isValidLine(String[] args) {
        if (args.length != 3) return false;
        return true;
    }

    public static boolean isUserLogged(User user) {
        if (user == null) return false;
        return true;
    }

    public static boolean doesTheProductExist(String name){
        TxtFileReader txtFileReader = new TxtFileReader(ApplicationConst.FILE_PRODUCTS_PATH);
        ArrayList<String> linesFromFile = txtFileReader.read();
        for(String line : linesFromFile){
            String[] args = line.split(" ");
            if(!isValidLine(args)) continue;
            if(args[0].equalsIgnoreCase(name)) return true;
        }
        System.out.println("The product does not exist!");
        return false;
    }

    public static boolean areEnoughItemsInStore(Store store,String productStr, int requiredQuantity){
        int currentQuantity = store.getProducts().get(productStr);
        if(currentQuantity - requiredQuantity < 0){
            System.out.println("There are not enough items! Current quantity is: " + currentQuantity);
            return false;
        }
        return true;
    }
}
