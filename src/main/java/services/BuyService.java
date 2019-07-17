package services;

import model.Product;
import model.Store;
import model.User;
import utils.ApplicationConst;
import utils.LoginUtil;
import utils.TxtFileReader;
import utils.TxtFileWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class BuyService {

    private User user;
    private Store store;
    private static final Logger loger = Logger.getLogger(Logger.class.getName());

    public BuyService(Store store, User user) {
        this.store = store;
        this.user = user;
    }

    public BuyService() {
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
            if (productsFromCart.isEmpty()) {
                loger.info("You don't have any products in cart!");
            } else {
                for (String productStr : productsFromCart.keySet()) {
                    if (LoginUtil.doesTheProductExist(productStr)) {
                        int priceForCurrentProduct = getPriceForCurrentProduct(productStr);
                        int totalPrice = productsFromCart.get(productStr) * priceForCurrentProduct;
                        System.out.println("\t" + productStr + " " + productsFromCart.get(productStr)
                                + " " + totalPrice);
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

    public int getPriceForCurrentProduct(String productName) {
        int quantity = 0;
        TxtFileReader txtFileReader = new TxtFileReader(ApplicationConst.FILE_PRODUCTS_PATH);
        ArrayList<String> lines = txtFileReader.read();
        for (String line : lines) {
            String[] args = line.split(" ");
            if (!LoginUtil.isValidLine(args)) continue;
            if (args[0].equalsIgnoreCase(productName)) {
                quantity = Integer.parseInt(args[2]);
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
                displayAvailableProducts();
                System.out.println("Choose a product.");

                try {
                    productToAddStr = scanner.nextLine();
                } catch (Exception e) {
                    continue;
                }
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

            updateAccountsFile(productToAddStr,numberOfItems);

            while (numberOfItems > 0) {
                user.addAndCountProduct(productToAddStr);
                numberOfItems--;
            }

            updateQuantity(productToAddStr, numberOfItems);
        } else {
            loger.info("You have to log first!");
        }
    }

    public void updateQuantity(String productStr, Integer quantity) {
        store.updateQuantityForAProduct(productStr, quantity);
    }


    public void updateAccountsFile(String productStr, int newQuantity) {

        updatedDataInAuxFile(productStr, newQuantity);
        deleteOldFile(ApplicationConst.FILE_PRODUCTS_PATH);
        renameFile(ApplicationConst.FILE_PRODUCTS_PATH_AUX, ApplicationConst.FILE_PRODUCTS_PATH);
    }



    public void updatedDataInAuxFile(String productStr, int newQuantity) {
        //creez un fisier auxiliar pentu a scrie in el
        TxtFileWriter txtFileWriter = new TxtFileWriter(ApplicationConst.FILE_PRODUCTS_PATH_AUX);

        //citesc fisierul in care sunt proddusele
        TxtFileReader fileReader = new TxtFileReader(ApplicationConst.FILE_PRODUCTS_PATH);
        ArrayList<String> listOfAccounts = fileReader.read();

        //scriu in noul fsier toate datele din vechiul fisier + ANTITATEA actualizata
        txtFileWriter.customWrite(listOfAccounts,productStr,newQuantity);
    }

    public void deleteOldFile(String fileAccountsPath) {
        //sterg vechiul fisier cu date
        try {
            Files.deleteIfExists(Paths.get(fileAccountsPath));
        } catch (NoSuchFileException e) {
            loger.warning("No such file/directory exists");
        } catch (DirectoryNotEmptyException e) {
            loger.warning("Directory is not empty.");
        } catch (IOException e) {
            loger.warning("Invalid permissions.");
        }
        loger.info("Deletion successful.");
    }

    public void renameFile(String fileAccountsPathAux, String fileAccountsPath) {
        // redenumesc fisierul auxiliar cu numele fisierului original
        Path source = Paths.get(fileAccountsPathAux);
        try {
            Files.move(source, source.resolveSibling(fileAccountsPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
