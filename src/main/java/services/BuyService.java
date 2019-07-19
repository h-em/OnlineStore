package services;

import model.Product;
import model.Store;
import model.User;
import utils.ApplicationConst;
import utils.Util;
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
        Map<Product, Integer> products = store.getProducts();
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
            if (Util.isValidLine(args)) {
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
        if (Util.isUserLogged(user)) {
            System.out.println("User " + user.getName());
            System.out.println("Shopping list: ");
            Map<Product, Integer> productsFromCart = user.getProductsFromCart();
            if (productsFromCart.isEmpty()) {
                loger.info("You don't have any products in cart!");
            } else {
                for (Product product : productsFromCart.keySet()) {
                    BigDecimal priceForCurrentProduct = product.getPrice();
                    BigDecimal totalPrice = priceForCurrentProduct.multiply(new BigDecimal(productsFromCart.get(product)));
                    System.out.println("\t" + product.getName() + " " + productsFromCart.get(product)
                            + " " + totalPrice);
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nPress any key...");
            scanner.nextLine();
        } else {
            loger.info("You have to log first!");
        }
    }

    public void buyProduct() {
        Scanner scanner = new Scanner(System.in);
        Product newProduct = null;
        if (Util.isUserLogged(user)) {
            boolean isValidProduct = false;
            boolean areEnoughItems = false;
            String productToAddStr = null;
            int numberOfItems = 0;
            while (!isValidProduct || !areEnoughItems) {

                displayAvailableProducts();

                if (!isValidProduct) {
                    System.out.println("Choose a product.");
                    try {
                        productToAddStr = scanner.nextLine();
                        while(!productToAddStr.matches("[a-zA-Z]+")){
                            System.out.println("Please enter a valid name!");
                            productToAddStr = scanner.nextLine();
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }

                if (!areEnoughItems) {
                    System.out.println("How many items do you want buy?.");
                    try {
                        numberOfItems = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("Is should be a number!");
                        continue;
                    }
                }

                isValidProduct = store.doesTheProductExist(productToAddStr);
                BigDecimal priceOfProduct = store.getPrice(productToAddStr);
                newProduct = new Product(productToAddStr, priceOfProduct);
                areEnoughItems = Util.areEnoughItemsInStore(store, newProduct, numberOfItems);

            }
            updateAccountsFile(productToAddStr, numberOfItems);
            updateQuantity(newProduct, numberOfItems);

            while (numberOfItems > 0) {
                user.addAndCountProduct(newProduct);
                numberOfItems--;
            }
        } else {
            loger.info("You have to log first!");
        }
    }

    private void updateQuantity(Product product, Integer quantity) {
        store.updateQuantityForAProduct(product, quantity);
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
        txtFileWriter.customWrite(listOfAccounts, productStr, newQuantity);
    }

    private void deleteOldFile(String fileAccountsPath) {
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

    private void renameFile(String fileAccountsPathAux, String fileAccountsPath) {
        // redenumesc fisierul auxiliar cu numele fisierului original
        Path source = Paths.get(fileAccountsPathAux);
        try {
            Files.move(source, source.resolveSibling(fileAccountsPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
