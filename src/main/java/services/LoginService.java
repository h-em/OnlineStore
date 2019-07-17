package services;

import menu.BuyMenu;
import model.Product;
import model.Store;
import model.User;
import utils.ApplicationConst;
import utils.LoginUtil;
import utils.TxtFileReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class LoginService {
    private User user;
    private Store store;
    private static final Logger loger = Logger.getLogger(Logger.class.getName());

    public LoginService(User user){
        this.user = user;
    }
    public LoginService(){}


    public User loginUser(String userId, String password) {
        User user = null;
        TxtFileReader txtFileReader = new TxtFileReader(ApplicationConst.FILE_USERS_PATH);
        ArrayList<String> lines = txtFileReader.read();

        for (String line : lines) {
            String[] tokens = line.split(" ");
            if (tokens.length != 2) {
                continue;
            }
            if (tokens[0].equals(userId) && tokens[1].equals(password)) {
                user = new User(userId, password);
                break;
            }
        }
        return user;
    }

    public void login(Integer option) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("User: ");
        String userId = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();

        User user = loginUser(userId, password);
        if (user == null) {
            loger.warning("UserId\\Password incorect!");
        } else {
            loger.info("Hello, " + userId + "!");
            BuyMenu buyMenu = new BuyMenu(user);
            while (option != 0) {
                buyMenu.displayOption();
                System.out.println("Your option: ");
                option = scanner.nextInt();
                try {
                    buyMenu.executeOption(option);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void displayAvailableProducts() {
        System.out.println("Products:");
        Map<Product, Integer> products = readAvailableProducts();
        if (products.isEmpty()) {
            System.out.println("No products in store!");
        } else {
            for (Product product : products.keySet()) {
                System.out.println("\t" + product.getName() + " " + products.get(product) + " " + product.getPrice());
            }
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPress any key...");
        scanner.nextLine();
    }

    public Map<Product, Integer> readAvailableProducts() {
        Map<Product, Integer> products = new HashMap<>();
        TxtFileReader txtFileReader = new TxtFileReader(ApplicationConst.FILE_PRODUCTS_PATH);
        ArrayList<String> lines = txtFileReader.read();
        for (String line : lines) {
            String[] args = line.split(" ");
            if (LoginUtil.isValidLine(args)) {
                String name = args[0];
                BigDecimal price = new BigDecimal(Integer.parseInt(args[2]));
                int quantity = Integer.parseInt(args[1]);
                Product product = new Product(name, price);
                products.put(product, quantity);
            }

        }
        return products;
    }


    public void displayShopingCart() {

        if (LoginUtil.isUserLogged(user)) {

            System.out.println("User " + user.getName());
            System.out.println("Shopping list:");

            Map<String, Integer> productsFromCart = user.getProductsFromCart();
            if (productsFromCart == null) {
                loger.info("You don't have any products in cart!");
            } else {
                for (String productStr : productsFromCart.keySet()) {
                    if (LoginUtil.doesTheProductExist(productStr)) {
                        int quantityForCurrentProduct = getQuantityForCurrentProduct(productStr);
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

    public int getQuantityForCurrentProduct(String productName) {
        int quantity = 0;
        TxtFileReader txtFileReader = new TxtFileReader(ApplicationConst.FILE_PRODUCTS_PATH);
        ArrayList<String> lines = txtFileReader.read();
        for (String line : lines) {
            String[] args = line.split(" ");
            if (!LoginUtil.isValidLine(args)) continue;
            if (args[0].equalsIgnoreCase(productName)) {
                quantity = Integer.parseInt(args[1]);
            }
        }
        return quantity;
    }


    public void buyProduct() {
        Scanner scanner = new Scanner(System.in);

        if (LoginUtil.isUserLogged(user)) {

            boolean isValidProduct = false;
            String productToAddStr = null;
            while (!isValidProduct) {
                System.out.println("Choose a product.");
                displayAvailableProducts();
                productToAddStr = scanner.nextLine();
                if (store.getProducts().containsKey(productToAddStr)) {
                    isValidProduct = true;
                } else {
                    System.out.println("The product does not exist!");
                }
            }

            int numberOfItems = 0;
            boolean areEnoughItems = false;
            while (!areEnoughItems) {
                System.out.println("How many items do you want buy?.");
                numberOfItems = scanner.nextInt();
                if (LoginUtil.areEnoughItemsInStore(store, productToAddStr, numberOfItems)) {
                    areEnoughItems = true;
                }
            }

            while (numberOfItems > 0) {
                user.addAndCountProduct(productToAddStr);
                numberOfItems--;
            }


        } else {
            loger.info("You have to log first!");
        }
    }

    public void chooseProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a product.");
        displayAvailableProducts();
        String productStr = scanner.nextLine();
    }
}
