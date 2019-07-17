package services;

import model.User;
import utils.LoginUtil;

import javax.jws.soap.SOAPBinding;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class BuyService {

    private User user;
    private static final Logger loger = Logger.getLogger(Logger.class.getName());


    public void displayShopingCart() {
        LoginService loginService = new LoginService(user);
        if (LoginUtil.isUserLogged(user)) {

            System.out.println("User " + user.getName());
            System.out.println("Shopping list:");

            Map<String, Integer> productsFromCart = user.getProductsFromCart();
            if (productsFromCart == null) {
                loger.info("You don't have any products in cart!");
            } else {
                for (String productStr : productsFromCart.keySet()) {
                    if (LoginUtil.doesTheProductExist(productStr)) {
                        int quantityForCurrentProduct = loginService.getQuantityForCurrentProduct(productStr);
                        System.out.println(productStr + " " + quantityForCurrentProduct +
                                " " + productsFromCart.get(productStr));
                    }
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nPress any key...");
            scanner.nextLine();
        } else {
            loger.info("You have to log first!");
        }
    }
}
